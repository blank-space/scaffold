package com.dawn.launch.app.task

import com.caij.app.startup.Task

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class InitWebViewTask : Task(){
    override fun run() {

    }

    override fun dependencies(): MutableList<Class<out Task>>? =null

    override fun getTaskName()= "InitWebViewTask"

    override fun isMustRunMainThread(): Boolean {
        return true
    }

    override fun isWaitOnMainThread(): Boolean {
        return false
    }
}