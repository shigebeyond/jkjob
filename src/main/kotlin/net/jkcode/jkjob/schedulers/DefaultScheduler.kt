package net.jkcode.jkjob.schedulers

import net.jkcode.jkjob.IJob
import net.jkcode.jkjob.ITrigger
import net.jkcode.jkjob.JobExprParser
import net.jkcode.jkjob.jobLogger
import net.jkcode.jkjob.trigger.CronTrigger
import net.jkcode.jkutil.common.AtomicStarter
import java.util.concurrent.ConcurrentHashMap

/**
 * 作业调度器
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-23 6:43 PM
 */
class DefaultScheduler() : IScheduler {

    /**
     * 单次启动者
     */
    protected val starter = AtomicStarter()

    /**
     * 缓存触发器
     */
    protected val triggers: ConcurrentHashMap<String, CronTrigger> = ConcurrentHashMap();

    /**
     * 添加定时作业
     */
    override fun addJob(cronExpr: String, job: IJob): ITrigger {
        // 由cron表达式构建触发器
        val trigger = triggers.getOrPut(cronExpr) {
            CronTrigger(cronExpr)
        }
        jobLogger.debug("由cron表达式[{}]构建触发器触发器: {}", cronExpr, trigger)
        // 添加作业
        trigger.addJob(job)
        // 启动触发器
        if(starter.isStarted)
            trigger.start()
        return trigger
    }

    /**
     * 启动所有触发器
     */
    override fun start(){
        starter.startOnce {
            for ((cron, trigger) in triggers){
                trigger.start()
            }
        }
    }

    /**
     * 关闭所有触发器
     */
    override fun shutdown() {
        if(starter.isStarted){
            for ((cron, trigger) in triggers){
                trigger.shutdown()
            }
        }
    }

}