package net.jkcode.jkjob.job

import net.jkcode.jkutil.common.generateId
import net.jkcode.jkjob.IJob

/**
 * 基础作业
 *   就是简单的实现了id属性
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-21 3:55 PM
 */
abstract class BaseJob(public override val id: Long = generateId("job") /* 作业标识，全局唯一 */) : IJob {

}