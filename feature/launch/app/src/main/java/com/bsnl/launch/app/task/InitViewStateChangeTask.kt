package com.bsnl.launch.app.task

import com.bsnl.common.ui.viewStatus.Gloading
import com.bsnl.common.ui.viewStatus.adapter.GlobalAdapter
import com.bsnl.faster.Task
import com.bsnl.launch.app.BuildConfig

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class InitViewStateChangeTask:Task() {
    override fun run() {
        Gloading.debug(BuildConfig.LOG_DEBUG)
        Gloading.initDefault(GlobalAdapter())
    }
}