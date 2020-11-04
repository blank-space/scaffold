package com.bsnl.launch.app.webview

import android.view.ViewGroup
import android.webkit.WebSettings

import android.webkit.WebView

import android.widget.LinearLayout
import com.bsnl.base.BaseApp


class WebViewPool private constructor() {
    private var currentSize = 0//设置 缓存模式(true);

    /**
     * 获取webview
     *
     */
    val webView: WebView?
        get() {
            synchronized(lock) {
                val webView: WebView?
                if (available.size > 0) {
                    webView = available[0]
                    available.removeAt(0)
                    currentSize++
                    inUse.add(webView)
                } else {
                    webView = WebView(BaseApp.application)
                    inUse.add(webView)
                    currentSize++
                }
                val params: ViewGroup.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                webView!!.layoutParams = params
                webView.settings.javaScriptEnabled = true
                webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE //设置 缓存模式(true);
                webView.settings.setAppCacheEnabled(false)
                webView.settings.setSupportZoom(false)
                webView.settings.useWideViewPort = true
                webView.settings.javaScriptCanOpenWindowsAutomatically = true
                webView.settings.domStorageEnabled = true
               // webView.loadUrl("about:blank")
                return webView
            }
        }

    /**
     * 回收webview ,不解绑
     * @param webView 需要被回收的webview
     *
     */
    fun removeWebView(webView: WebView?) {
        var webView = webView
        webView!!.loadUrl("about:blank")
        webView.stopLoading()
        webView.setWebChromeClient(null)
        webView.webViewClient = null
        webView.clearCache(true)
        webView.clearHistory()
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE //设置 缓存模式(true);
        webView.settings.setAppCacheEnabled(false)
        webView.settings.setSupportZoom(false)
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webView.settings.loadWithOverviewMode = false
        webView.settings.setUserAgentString("android_client")
        webView.settings.defaultTextEncodingName = "UTF-8"
        webView.settings.defaultFontSize = 16
        synchronized(lock) {
            inUse.remove(webView)
            if (available.size < maxSize) {
                available.add(webView)
            } else {
                webView = null
            }
            currentSize--
        }
    }

    /**
     * 回收webview ,解绑
     * @param webView 需要被回收的webview
     *
     */
    fun removeWebView(view: ViewGroup, webView: WebView?) {
        var webView = webView
        view.removeView(webView)
        webView!!.loadUrl("about:blank")
        webView.stopLoading()
        webView.setWebChromeClient(null)
        webView.webViewClient = null
        webView.clearCache(true)
        webView.clearHistory()
        webView.settings.javaScriptEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE //设置 缓存模式(true);
        webView.settings.setAppCacheEnabled(false)
        webView.settings.setSupportZoom(false)
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        webView.settings.loadWithOverviewMode = false
        webView.settings.setUserAgentString("android_client")
        webView.settings.defaultTextEncodingName = "UTF-8"
        webView.settings.defaultFontSize = 16
        synchronized(lock) {
            inUse.remove(webView)
            if (available.size < maxSize) {
                available.add(webView)
            } else {
                webView = null
            }
            currentSize--
        }
    }

    /**
     * 设置webview池个数
     * @param size webview池个数
     */
    fun setMaxPoolSize(size: Int) {
        synchronized(lock) { maxSize = size }
    }

    companion object {
        private const val DEMO_URL = "https://www.baidu.com"
        private var available: MutableList<WebView?> = ArrayList()
        private var inUse: MutableList<WebView?> = ArrayList()
        private val lock = byteArrayOf()
        private var maxSize = 3

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

        /**
         * Webview 初始化
         * 最好放在application oncreate里
         */
        fun init() {
            for (i in 0 until maxSize) {
                val webView = WebView(BaseApp.application)
                val params: ViewGroup.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                webView.layoutParams = params
                webView.settings.javaScriptEnabled = true
                webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE //设置 缓存模式(true);
                webView.settings.setAppCacheEnabled(false)
                webView.settings.setSupportZoom(false)
                webView.settings.useWideViewPort = true
                webView.settings.javaScriptCanOpenWindowsAutomatically = true
                webView.settings.domStorageEnabled = true
                webView.loadUrl(DEMO_URL)
                available.add(webView)
            }
        }
    }

    init {
        available = ArrayList()
        inUse = ArrayList()
    }
}