package com.bsnl.launch.app.task

import com.bsnl.base.BaseApp
import com.bsnl.faster.Task
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
}