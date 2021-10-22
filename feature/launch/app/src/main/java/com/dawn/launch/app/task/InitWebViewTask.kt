package com.dawn.launch.app.task

import com.dawn.base.BaseApp
import com.dawn.base.widget.webview.WebViewPool
import com.dawn.faster.MainTask

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class InitWebViewTask : MainTask(){
    override fun run() {
        WebViewPool.init(BaseApp.application)
    }
}