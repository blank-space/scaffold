package com.dawn.webview.view

import android.view.View
import com.dawn.webview.iface.IWebChromeClient
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView

class XWebChromeClient(private val webChromeClient: IWebChromeClient) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        webChromeClient.onProgressChanged(view, newProgress)

    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        webChromeClient.onReceivedTitle(view, title)

    }
}