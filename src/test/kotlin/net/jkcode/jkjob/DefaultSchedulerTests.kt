package net.jkcode.jkjob

import net.jkcode.jkjob.schedulers.DefaultScheduler
import org.junit.Test

/**
 * 作业表达式解析
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-24 2:27 PM
 */
class DefaultSchedulerTests: BaseJobTests() {

    @Test
    fun testLaunch(){
        try {
            val cronJobExpr = "0/10 * * * * ? -> lpc net.jkcode.jkjob.LocalBean sayHi(String) (\\\"测试消息\\\")"
            //val cronJobExpr = "0/10 * * * * ? -> rpc net.jkcode.jksoa.rpc.example.ISimpleService sayHi(String) (\"测试消息\")"
            val scheduler = DefaultScheduler()
            trigger = scheduler.addJob(cronJobExpr)
            scheduler.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}