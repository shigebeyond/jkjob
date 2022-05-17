package net.jkcode.jkjob

import net.jkcode.jkjob.job.InvocationJob
import net.jkcode.jkutil.invocation.Invocation
import net.jkcode.jkutil.invocation.ShardingInvocation
import org.junit.Test

/**
 * 作业表达式解析
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-24 2:27 PM
 */
class JobExprTests: BaseJobTests() {

    fun toAndParseExpr(job: IJob){
        try {
            // 生成表达式
            val expr = job.toExpr()
            println("生成作业表达式: $expr")
            // 解析表达式
            val job2 = net.jkcode.jkjob.JobExprParser.parse(expr)
            println("解析作业表达式: $job2")
            // 触发作业
            buildPeriodicTrigger(job)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    @Test
    fun testLpcJobExpr(){
        val inv = Invocation(LocalBean::sayHi, arrayOf<Any?>("测试消息"))
        val job = InvocationJob(inv)
        toAndParseExpr(job)
    }

    @Test
    fun testShardingLpcJobExpr(){
        val args:Array<Any?> = Array(3) { i ->
            "第${i}个分片的参数" // ISimpleService::sayHi 的实参
        }
        val inv = ShardingInvocation(LocalBean::sayHi, args, 1)
        val job = InvocationJob(inv)
        toAndParseExpr(job)
    }

}