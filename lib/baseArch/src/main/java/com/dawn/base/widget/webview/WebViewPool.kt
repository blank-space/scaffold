package com.dawn.base.widget.webview

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout


class WebViewPool private constructor() {
    init {
        available = ArrayList()
        inUse = ArrayList()
    }

    private var currentSize = 0

    val mWebView: WebView?
        get() {
            synchronized(lock) {
                var webView: WebView? = null
                if (available.size > 0) {
                    webView = available[0]
                    available.removeAt(0)
                    currentSize++
                    inUse.add(webView)
                } else {
                    if (application != null) {
                        webView = WebView(application!!)
                        setupWebView(webView)
                        inUse.add(webView)
                        currentSize++
                    }
                }
                return webView
            }
        }


    fun removeWebView(web: WebView?) {
        web?.let {
            val parent = it.getParent()
            if (parent != null) {
                (parent as ViewGroup).removeView(it)
            }
            it.stopLoading()
            it.settings.javaScriptEnabled = false
            it.clearHistory()
            it.loadUrl(BLANK_URL)
            it.removeAllViews()
            it.destroy()
        }
    }


    /** 设置WebView池个数 */
    fun setMaxPoolSize(size: Int) {
        synchronized(lock) { maxSize = size }
    }

    companion object {
        private const val BLANK_URL = "about:blank"
        private var available: MutableList<WebView?> = ArrayList()
        private var inUse: MutableList<WebView?> = ArrayList()
        private val lock = byteArrayOf()
        private var maxSize = 3
        private var application: Application? = null

        @Volatile
        var instance: WebViewPool? = null
            get() {
                if (field == null) {
                    synchronized(WebViewPool::class.java) {
                        if (field == null) {
                            field = WebViewPool()
                        }
                    }
                }
                return field
            }
            private set

        /** 初始化 ，建议放在application#onCreate */
        fun init(app: Application) {
            application = app
            for (i in 0 until maxSize) {
                val webView = WebView(app)
                setupWebView(webView)
                available.add(webView)
            }
        }


        @SuppressLint("SetJavaScriptEnabled")
        fun setupWebView(webView: WebView) {
            val params: ViewGroup.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webView.let {
                it.settings.apply {
                    allowFileAccess = false
                    //设置自适应屏幕，useWideViewPort+loadWithOverviewMode两者合用
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    javaScriptEnabled = true
                    loadsImagesAutomatically = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    }
                }
                it.layoutParams = params
            }
        }
    }


}