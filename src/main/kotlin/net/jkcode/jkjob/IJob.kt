package net.jkcode.jkjob

/**
 * 作业
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-21 3:06 PM
 */
interface IJob {

    /**
     * 作业标识，全局唯一
     */
    val id: Long

    /**
     * 执行作业
     *
     * @param context 作业执行的上下文
     * @return 异步则返回CompletableFuture, 否则返回null即可
     */
    fun execute(context: IJobExecutionContext): Any?

    /**
     * 转为作业表达式
     * @return
     */
    fun toExpr(): String {
        return "custom " + javaClass.name
    }

    /**
     * 记录作业执行异常
     *   可记录到磁盘中,以便稍后重试
     * @param e
     */
    fun logExecutionException(e: Throwable){
    }

}