package com.dawn.webview.view

import androidx.appcompat.app.AlertDialog
import com.dawn.webview.jsbridge.BridgeWebView
import com.dawn.webview.jsbridge.BridgeWebViewClient
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.WebView

class XWebViewClient(web: BridgeWebView) : BridgeWebViewClient(web) {

    override fun onReceivedSslError(webView: WebView?, p1: SslErrorHandler?, p2: SslError?) {
        webView?.let {
            it.context.apply {
                AlertDialog.Builder(this).apply {
                    setMessage("SSL认证失败，是否继续访问？")
                    setPositiveButton("确定") { _, _ ->
                        p1?.proceed()
                    }
                    setNegativeButton("取消") { _, _ ->
                        p1?.cancel()
                    }
                    create().show()
                }
            }
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.settings?.let {
            if (!it.loadsImagesAutomatically) {
                it.loadsImagesAutomatically = true
            }
        }
    }

}