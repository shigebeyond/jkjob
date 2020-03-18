package net.jkcode.jkjob.job

import net.jkcode.jkjob.IJobExecutionContext

/**
 * 用lambda封装内容的作业
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-23 7:56 PM
 */
class LambdaJob(protected val lambda: (net.jkcode.jkjob.IJobExecutionContext) -> Unit) : BaseJob() {

    /**
     * 执行作业
     * @param context 作业执行的上下文
     * @return 异步则返回CompletableFuture, 否则返回null即可
     */
    public override fun execute(context: IJobExecutionContext):Any?{
        lambda(context)
        return null
    }
}