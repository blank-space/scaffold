package com.dawn.launch.app.task

import com.dawn.base.log.L
import com.dawn.faster.Task

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