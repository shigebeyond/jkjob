package net.jkcode.jkjob

import net.jkcode.jkjob.job.InvocationJob
import net.jkcode.jkutil.common.getMethodBySignature
import net.jkcode.jkutil.invocation.Invocation
import net.jkcode.jkutil.invocation.ShardingInvocation
import org.junit.Test

class LpcJobTests: BaseJobTests(){

    @Test
    fun testLpc(){
        try {
            val c = Class.forName("fuck")

//        val c = SimpleService::class.java
//        val m = c.getMethod("sayHi", String::class.java)
//        println(m.getSignature())

            val m = c.getMethodBySignature("hostname()")
            val bean = c.newInstance()
            val result = m!!.invoke(bean)
            println(result)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    @Test
    fun testLpcJob(){
        val inv = Invocation(LocalBean::sayHi, arrayOf<Any?>("测试消息"))
        val job = InvocationJob(inv)
        buildPeriodicTrigger(job)
    }

    @Test
    fun testShardingLpcJob(){
        val args:Array<Any?> = Array(3) { i ->
            "第${i}个分片的参数" // ISimpleService::sayHi 的实参
        }
        val inv = ShardingInvocation(LocalBean::sayHi, args, 1)
        val job = InvocationJob(inv)
        buildPeriodicTrigger(job)
    }

    // 放到 jksoa-rpc-client 工程中
    /*@Test
    fun testRpcJob(){
        val req = RpcRequest(ISimpleService::sayHi, arrayOf<Any?>("测试消息"))
        val job = InvocationJob(req)
        buildCronTrigger(job)
    }

    @Test
    fun testShardingRpcJob(){
        val args:Array<Any?> = Array(3) { i ->
            "第${i}个分片的参数" // ISimpleService::sayHi 的实参
        }
        val req = ShardingRpcRequest(ISimpleService::sayHi, args, 1)
        val job = InvocationJob(req)
        buildPeriodicTrigger(job)
    }*/


}





