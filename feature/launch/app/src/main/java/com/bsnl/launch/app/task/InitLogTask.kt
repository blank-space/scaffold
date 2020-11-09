package com.bsnl.launch.app.task

import com.bsnl.base.BaseApp
import com.bsnl.base.log.L
import com.bsnl.faster.Task
import com.tencent.mmkv.MMKV

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
}