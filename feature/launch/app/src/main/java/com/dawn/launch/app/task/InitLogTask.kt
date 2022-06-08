
package com.dawn.launch.app.task

import com.caij.app.startup.Task
import com.dawn.base.log.L


/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */

class InitLogTask : Task(){
    override fun run() {
        //日志框架
        L.init()
    }

    override fun dependencies(): MutableList<Class<out Task>>? {
       return  null
    }

    override fun getTaskName(): String {
        return "InitLogTask"
    }
}
