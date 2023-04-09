package net.jkcode.jkjob.schedulers

import net.jkcode.jkjob.IJob
import net.jkcode.jkjob.ITrigger
import net.jkcode.jkjob.JobExprParser
import net.jkcode.jkjob.jobLogger

/**
 * 作业调度器接口
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-23 6:43 PM
 */
interface IScheduler {

    /**
     * 添加定时作业
     * @param cronJobExpr cron与作业的复合表达式, 由cron表达式 + 作业表达式组成, 其中作业表达式前面加`:`, 标识触发的内容是作业
     *                    如 "0/10 * * * * ? -> lpc net.jkcode.jksoa.rpc.example.SimpleService hostname() ()"
     * @return
     */
    public fun addJob(cronJobExpr: String): ITrigger {
        // 分隔cron表达式 + 作业表达式
        val (cronExpr, jobExpr) = cronJobExpr.split("\\s*->\\s*".toRegex())
        // 添加定时作业
        return addJob(cronExpr, jobExpr)
    }

    /**
     * 添加定时作业
     * @param cronExpr cron表达式
     * @param jobExpr 作业表达式
     * @return
     */
    public fun addJob(cronExpr: String, jobExpr: String): ITrigger {
        // 由作业表达式解析作业
        val job = JobExprParser.parse(jobExpr)
        jobLogger.debug("由作业表达式[{}]解析作业: {}", jobExpr, job)

        // 添加定时作业
        return addJob(cronExpr, job)
    }

    /**
     * 添加定时作业
     * @param cronExpr cron表达式
     * @param job 作业
     * @return
     */
    fun addJob(cronExpr: String, job: IJob): ITrigger

    /**
     * 启动所有触发器
     */
    fun start()

    /**
     * 关闭所有触发器
     */
    fun shutdown()
}