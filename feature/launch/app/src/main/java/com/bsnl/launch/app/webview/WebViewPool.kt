package com.bsnl.launch.app.webview

import android.os.Build
import android.os.Build.VERSION_CODES.KITKAT
import android.view.ViewGroup
import android.webkit.WebSettings

import android.webkit.WebView

import android.widget.LinearLayout
import com.bsnl.base.BaseApp
import com.bsnl.base.log.L


class WebViewPool private constructor() {
    init {
        available = ArrayList()
        inUse = ArrayList()
    }

    private var currentSize = 0

    /**
     * 获取webview
     *
     */
    val mWebView: WebView?
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
                    setupWebView(webView)
                    inUse.add(webView)
                    currentSize++
                }
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

        webView?.apply {
            loadUrl("about:blank")
            stopLoading()
            setWebChromeClient(null)
            webViewClient = null
            clearCache(true)
            clearHistory()

            synchronized(lock) {
                inUse.remove(this)
                if (available.size < maxSize) {
                    available.add(this)
                } else {
                    webView = null
                }
                currentSize--
            }

        }

    }

    /**
     * 回收webview ,解绑
     * @param webView 需要被回收的webview
     *
     */
    fun removeWebView(view: ViewGroup?, web: WebView?) {
        var webView = web
        webView?.apply {
            view?.removeView(this)
            loadUrl("about:blank")
            stopLoading()
            setWebChromeClient(null)
            webViewClient = null
            clearCache(true)
            clearHistory()

            synchronized(lock) {
                inUse.remove(this)
                if (available.size < maxSize) {
                    available.add(this)
                } else {
                    webView = null
                }
                currentSize--
            }

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
        private const val DEMO_URL = "about:blank"
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
                setupWebView(webView)
                available.add(webView)
            }
        }


        fun setupWebView(webView: WebView) {
            val params: ViewGroup.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webView.layoutParams = params
            webView.settings.javaScriptEnabled = true
            //设置自适应屏幕，两者合用
            webView.settings.setUseWideViewPort(true) //将图片调整到适合webview的大小
            webView.settings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小
            //缩放操作
            webView.settings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
            webView.settings.setBuiltInZoomControls(true) //设置内置的缩放控件。若为false，则该WebView不可缩放
            webView.settings.setDisplayZoomControls(false) //隐藏原生的缩放控件
            webView.settings.domStorageEnabled = true
            webView.settings.setCacheMode(WebSettings.LOAD_DEFAULT)

            if (Build.VERSION.SDK_INT >= KITKAT) {
                //4.4以上系统在onPageFinished时再恢复图片加载
                webView.getSettings().setLoadsImagesAutomatically(true)
            } else {
                //设置网页在加载的时候暂时不加载图片
                webView.getSettings().setLoadsImagesAutomatically(false)
            }

            //设置是否开启密码保存功能，不建议开启，默认已经做了处理，存在盗取密码的危险
            webView.getSettings().setSavePassword(false)

            // 处理https当中不能加载http资源
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.getSettings().setBlockNetworkImage(false)

            webView.canGoBack()


        }
    }


}