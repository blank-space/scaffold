package com.dawn.webview.view

import android.content.Context
import com.dawn.webview.jsbridge.BridgeWebView
import com.tencent.smtt.sdk.WebView

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/18
 * @desc   : 监听滚动
 */
class XWebView(context: Context) : BridgeWebView(context) {
    var onScrollChangeListener: OnScrollChangeListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollChangeListener?.onScrollChange(this,l, t, oldl, oldt)
    }

    interface OnScrollChangeListener {
        fun onScrollChange(
            v: WebView?, scrollX: Int, scrollY: Int,
            oldScrollX: Int, oldScrollY: Int
        )
    }
}