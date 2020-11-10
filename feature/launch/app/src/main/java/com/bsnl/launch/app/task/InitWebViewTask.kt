package com.bsnl.launch.app.task

import com.bsnl.faster.MainTask
import com.bsnl.base.webview.WebViewPool

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