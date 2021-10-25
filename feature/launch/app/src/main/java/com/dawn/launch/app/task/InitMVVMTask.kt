package com.dawn.launch.app.task

import com.caij.app.startup.Task
import com.dawn.base.BaseApp
import com.tencent.mmkv.MMKV

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class InitMVVMTask : Task(){
    override fun run() {
        //本地Key-value存储
        MMKV.initialize(BaseApp.application)
    }

    override fun dependencies(): MutableList<Class<out Task>>? {
        return null
    }

    override fun getTaskName()= "InitMVVMTask"
}