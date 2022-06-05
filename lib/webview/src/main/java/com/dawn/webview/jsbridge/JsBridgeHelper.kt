package com.dawn.webview.jsbridge

import com.dawn.webview.view.XWebView


/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/17
 * @desc   :
 */
object JsBridgeHelper {

    /** JS调用Native，预先注册 */
    var registerHandlers = mutableMapOf<String, BridgeHandler>()
        set(value) {
            field.clear()
            field = value
        }

    var webView: XWebView?=null
}