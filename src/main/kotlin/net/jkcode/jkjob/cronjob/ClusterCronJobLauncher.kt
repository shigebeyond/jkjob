package net.jkcode.jkjob.cronjob

import net.jkcode.jkutil.leader.ZkLeaderElection

/**
 * 集群实现的作业启动器
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-29 11:48 AM
 */
abstract class ClusterCronJobLauncher {

    /**
     * 启动
     */
    public fun lauch(){
        // 选举领导者: 只有一个启动作业执行
        val election = ZkLeaderElection("cronJob")
        election.run(){
            // 加载cron与作业的复合表达式
            for(cronJobExpr in loadCronJobs()) {
                // 启动作业
                CronJobLauncher.lauch(cronJobExpr)
            }
        }
    }

    /**
     * 加载cron与作业的复合表达式
     * @return
     */
    public abstract fun loadCronJobs(): List<String>
}