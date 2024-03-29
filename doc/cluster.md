# Cluster

在 `net.jkcode.jkjob.schedulers.ClusterScheduler` 中实现了调度者集群.

集群中有多个候选者节点, 但是只有选为leader的候选者节点才能成为唯一的调度者, 其他候选者节点则成为热备.

他的实现很简单, 就是使用`net.jkcode.jksoa.leader.ZkLeaderElection`来选举leader作为调度者.

```
package net.jkcode.jkjob.schedulers

import net.jkcode.jksoa.leader.ZkLeaderElection

/**
 * 集群实现的作业调度器
 *    代理调用 DefaultScheduler 实例
 *    所有机器都可以各自添加job, 但只有zk选出的唯一一个能启动定时触发器, 没选中的添加job也没有也不执行
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-29 11:48 AM
 */
abstract class ClusterScheduler(
        protected val scheduler: DefaultScheduler = DefaultScheduler() // 代理对象
    ): IScheduler by scheduler {

    /**
     * 启动所有触发器
     */
    public override fun start(){
        // 选举领导者: 只有一个启动作业执行
        val election = ZkLeaderElection("scheduler")
        election.run(){
            // 加载cron与作业的复合表达式
            for(cronJobExpr in loadCronJobs()) {
                // 添加作业
                scheduler.addJob(cronJobExpr)
            }
            // 启动所有定时触发器
            scheduler.start()
        }
    }

    /**
     * 加载cron与作业的复合表达式
     * @return
     */
    public abstract fun loadCronJobs(): List<String>
}
```

开发者需自行实现 `loadCronJobs()`