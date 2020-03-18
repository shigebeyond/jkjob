package net.jkcode.jkjob

import net.jkcode.jkjob.cronjob.CronJobLauncher
import org.junit.Test

/**
 * 作业表达式解析
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-24 2:27 PM
 */
class CronJobLauncherTests: BaseJobTests() {

    @Test
    fun testLaunch(){
        try {
            val cronJobExpr = "0/10 * * * * ? -> lpc net.jkcode.jkjob.LocalBean echo(String) (\\\"测试消息\\\")"
            //val cronJobExpr = "0/10 * * * * ? -> rpc net.jkcode.jksoa.rpc.example.ISimpleService echo(String) (\"测试消息\")"
            trigger = CronJobLauncher.lauch(cronJobExpr)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}