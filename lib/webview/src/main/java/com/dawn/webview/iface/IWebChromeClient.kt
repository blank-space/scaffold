package com.dawn.webview.iface

import com.tencent.smtt.sdk.WebView

interface IWebChromeClient {

    fun onProgressChanged(view: WebView?, newProgress: Int)

    fun onReceivedTitle(view: WebView?, title: String?)
}