package com.bsnl.launch.app.task

import com.bsnl.base.BaseApp
import com.bsnl.base.log.L
import com.bsnl.faster.MainTask
import com.bsnl.faster.Task
import com.bsnl.launch.app.webview.WebViewPool
import com.tencent.mmkv.MMKV

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class InitWebViewTask : MainTask(){
    override fun run() {
        WebViewPool.init()
    }
}