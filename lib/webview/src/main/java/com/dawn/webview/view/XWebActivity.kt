package com.dawn.webview.view

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dawn.statusbar.*
import com.dawn.webview.databinding.ActivityXwebBinding
import com.dawn.webview.iface.IWebChromeClient
import com.dawn.webview.jsbridge.JsBridgeHelper
import com.tencent.smtt.sdk.WebView

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/10
 * @desc   :
 */
private const val MAX_PROGRESS = 80
const val URL = "url"
class XWebActivity : AppCompatActivity() {
    private val binding by lazy { ActivityXwebBinding.inflate(layoutInflater) }
    private var url: String? = null
    private var webView = WebViewPool.instance?.mWebView
    private val scrollHeight = 142.dp
    private val Int.dp get() = this.toFloat().dp.toInt()


    private val Float.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

    private val xWebViewClient by lazy {
        object : IWebChromeClient {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressBar.apply {
                    visibility = if (newProgress >= MAX_PROGRESS) View.GONE else View.VISIBLE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                binding.tvTitle.text = title
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initStatusBar() {
        immersiveStatusBar()
        immersiveNavigationBar()
        fitStatusBar(true)
        setStatusBarColor(Color.parseColor("#00000000"))
        setLightStatusBar(true)
    }

    private fun initView() {
        webView?.let { it ->
            Log.e("@@", " it.x5WebViewExtension:${it.x5WebViewExtension}")
            JsBridgeHelper.webView = it
            //不显示边界回弹效果
            it.overScrollMode = View.OVER_SCROLL_NEVER
            it.webChromeClient = XWebChromeClient(xWebViewClient)
            it.webViewClient = XWebViewClient(it)

            if (JsBridgeHelper.registerHandlers.isNotEmpty()) {
                JsBridgeHelper.registerHandlers.forEach { map ->
                    it.registerHandler(map.key, map.value)
                }
            }

            url?.let { it1 ->
                it.loadUrl(it1)
            }

            binding.flWebView.apply {
                removeAllViews()
                addView(it)
            }
        }


    }

    private fun initData() {
        url = intent.getStringExtra(URL)
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.onScrollChangeListener = null
        JsBridgeHelper.webView = null
        WebViewPool.instance?.removeWebView(webView)
        webView = null
    }

    companion object {
        fun startAction(context: Context, url: String) {
            val intent = Intent(context, XWebActivity::class.java)
            intent.putExtra(URL, url)
            context.startActivity(intent)
        }
    }
}