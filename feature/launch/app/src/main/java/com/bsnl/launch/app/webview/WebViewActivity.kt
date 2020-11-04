package com.bsnl.launch.app.webview


import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.base.log.L
import com.bsnl.common.utils.startActivity
import com.bsnl.launch.app.R


class WebViewActivity : AppCompatActivity() {
    protected var mWebviewRoot: FrameLayout? = null
    protected var mWebView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_launch_app_activity_web)
        mWebviewRoot = findViewById(R.id.webview_root_fl)
        mWebView = WebViewPool.instance?.webView

        mWebviewRoot!!.addView(mWebView)
        mWebView?.visibility =View.GONE
        initClient()

        initUrl()
    }

    private fun initClient() {
        mWebView?.setWebViewClient(WebViewClient())
        mWebView?.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress >=70) {
                    mWebView?.visibility =View.VISIBLE
                }
                super.onProgressChanged(view, newProgress)
            }
        })
    }

    private fun initUrl() {
        mWebView?.loadUrl(intent.getStringExtra(URL))
    }

    override fun onDestroy() {
        mWebviewRoot?.let { WebViewPool.instance?.removeWebView(it, mWebView) }
        super.onDestroy()
    }

    companion object{
        const val URL ="url"
        fun startAction(context: Context, url: String){
            startActivity<WebViewActivity>(context){
                putExtra(URL, url)
            }
        }

    }
}