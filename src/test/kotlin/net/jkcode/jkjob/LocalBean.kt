package net.jkcode.jkjob

/**
 * 测试的bean类
 * @author shijianhang<772910474@qq.com>
 * @date 2019-01-24 10:08 PM
 */
class LocalBean {

    public fun sayHi(msg: String): String{
        jobLogger.debug("调用本地bean的方法: sayHi(\"{}\")", msg)
        return msg
    }

}