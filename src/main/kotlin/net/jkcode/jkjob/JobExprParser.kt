package net.jkcode.jkjob

import net.jkcode.jkutil.common.getMethodByClassAndSignature
import net.jkcode.jkutil.validator.ArgsParser
import net.jkcode.jkutil.invocation.IInvocation
import net.jkcode.jkutil.invocation.Invocation
import net.jkcode.jkutil.invocation.ShardingInvocation
import net.jkcode.jkjob.job.InvocationJob
import java.lang.reflect.Method

/**
 * 作业表达式的解析器
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-21 3:06 PM
 */
object JobExprParser: IJobParser {

    /**
     * 编译作业表达式
     *     作业表达式是由4个元素组成, 4个元素之间以空格分隔: 1 作业类型 2 类名 3 方法签名 4 方法实参列表
     *     当作业类型是custom, 则后面两个元素为空
     *     方法实参列表, 是以()包围多个参数, 参数之间用`,`分隔
     *     格式为: lpc net.jkcode.jksoa.rpc.example.SimpleService sayHi(String) ("hello")
     * <code>
     *     val job = IJobFactory::parseJob("lpc net.jkcode.jksoa.rpc.example.SimpleService sayHi(String) ("hello")");
     * </code>
     *
     * @param expr 作业表达式
     * @return
     */
    public override fun parse(expr:String): IJob {
        if(expr.isEmpty())
            throw net.jkcode.jkjob.JobException("作业表达式为空")

        // 解析元素
        // 作业表达式是由4个元素组成, 4个元素之间以空格分隔: 1 作业类型 2 类名 3 方法签名 4 方法实参列表
        // 1 当作业类型是custom, 则后面两个元素为空
        if(expr.startsWith("custom")){ // 自定义的作业类型
            val subexprs = expr.split(' ', limit = 2)
            if(subexprs.size != 2)
                throw JobException("自定义的作业表达式是由2个元素组成, 2个元素之间以空格分隔: 1 作业类型 2 自定义的作业类名")
            val clazz = subexprs[1]
            val c = Class.forName(clazz)
            return c.newInstance() as IJob
        }

        // 2 其他作业类型: lpc / rpc / shardingLpc / shardingRpc
        val subexprs = expr.split(' ', limit = 5)
        if(subexprs.size < 4)
            throw JobException("其他作业表达式是由5个元素组成, 5个元素之间以空格分隔: 1 作业类型 2 类名 3 方法签名 4 方法实参列表 5 分片处理之每个分片的参数个数, 非分片处理可省略")
        val type: String = subexprs[0]
        val clazz: String = subexprs[1]
        val methodSignature: String = subexprs[2]
        val argsExpr: String = subexprs[3]
        val argsPerSharding: Int = if(subexprs.size == 4) 0 else subexprs[4].toInt()

        val method = getMethodByClassAndSignature(clazz, methodSignature)
        val args = ArgsParser.parse(argsExpr, method).toTypedArray()
        val inv: IInvocation = when(type){
            "lpc" -> Invocation(method, args)
            "shardingLpc" -> ShardingInvocation(method, args, argsPerSharding)

            //"rpc" -> RpcRequest(method, args)
            "rpc" -> createRpcInvocation("net.jkcode.jksoa.common.RpcRequest", method, args) as Invocation
            //"shardingRpc" -> ShardingRpcRequest(method, args, argsPerSharding)
            "shardingRpc" -> createRpcInvocation("net.jkcode.jksoa.common.ShardingRpcRequest", method, args) as Invocation
            else -> throw JobException("无效作业类型: $type")
        }
        return InvocationJob(inv)
    }

    /**
     * @param className rpc调用实现类, net.jkcode.jksoa.common.RpcRequest / net.jkcode.jksoa.common.ShardingRpcRequest
     * @param func 方法
     * @param args 实参
     * @return
     */
    private fun createRpcInvocation(className: String, method: Method, args: Array<Any?>): IInvocation {
        // 获得类
        val clazz = Class.forName(className)
        // 获得构造函数
        val constructor = clazz.getConstructor(Method::class.java, Array<Any?>::class.java)
        // 实例化
        return constructor.newInstance(method, args) as IInvocation

    }

}