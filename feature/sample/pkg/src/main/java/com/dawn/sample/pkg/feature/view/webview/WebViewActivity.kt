package com.dawn.sample.pkg.feature.view.webview


import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.widget.webview.WebViewPool
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.feature.constant.Bundle_URL

@Route(path = SamplePath.A_WEBVIEW_ACTIVITY)
class WebViewActivity : AppCompatActivity() {
    protected var mWebviewRoot: FrameLayout? = null
    protected var mWebView: WebView? = null

    @JvmField
    @Autowired(name = Bundle_URL)
    var mUrl: String? = null

    private var startUrl: String? = null
    private val pb_web : ProgressBar by lazy {
        findViewById(R.id.pb_web)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        DisplayUtils.setFitsSystemWindows(this, false)
        setContentView(R.layout.feature_sample_pkg_activity_web)
        mWebviewRoot = findViewById(R.id.webview_root_fl)
        mWebView = WebViewPool.instance?.mWebView
        mWebviewRoot!!.addView(mWebView)
        mWebView?.visibility = View.GONE

        getData()
        initClient()
        initUrl()
    }

    private fun initClient() {
        mWebView?.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                // //页面finish后再发起图片加载
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                pb_web.visibility = View.VISIBLE

                //重定向后url不一致
                if (!url!!.startsWith(startUrl!!)) {
                    finish()
                }

            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                if (errorCode == 404) {
                    val data = "Page No Found"
                    view?.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                } else {
                    //自定义处理
                    /* if (webListener!=null){
                        webListener.showErrorView();
                     }*/
                }
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)

                //https忽略证书问题
                handler?.proceed()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                mUrl = url
                return false
            }


            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {


                return super.shouldInterceptRequest(view, request)
                /* val builder = Request.Builder()
                 L.d("request?.method:${request?.method.toString()}")
                 builder.url(mUrl).method(request?.method, null)
                 val requestHeaders = request?.getRequestHeaders()
                 if (!requestHeaders.isNullOrEmpty()) {
                     for ((index, e) in requestHeaders) {
                         builder.addHeader(index, e)
                     }
                 }
                 val okHttpClient = OkHttpClient.Builder()
                     .followRedirects(false) // 不follow重定向
                     .followSslRedirects(false)
                     .connectTimeout(15, TimeUnit.SECONDS)
                     .build()
                 val synCall = okHttpClient.newCall(builder.build())
                 val response = synCall.execute()
                 val code = response?.code()

                 code?.let {
                     if (it > 299 && it < 400) {
                         //重定向
                         return super.shouldInterceptRequest(view, request)
                     }
                 }

                 response?.let {
                     val body = it.body()
                     val map = mutableMapOf<String, String>()
                     for (index in 0..it.headers().size() - 1) {
                         map[it.headers().name(index)] = it.headers().value(index)
                     }
                     var mimeType = MimeTypeMap.getSingleton()
                         .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(mUrl))
                     val contentType = response.headers()["Content-Type"]
                     var encoding = "utf-8"

                     //获取ContentType和编码格式
                     if (contentType != null && !"".equals(contentType)) {
                         if (contentType.contains(";")) {

                             val args = contentType.split(";")
                             mimeType = args[0]
                             val args2 = args[1].trim().split("=")
                             if (args.size == 2 && args2[0].trim().toLowerCase()
                                     .equals("charset")
                             ) {
                                 encoding = args2[1].trim();
                             }
                         } else {
                             mimeType = contentType;
                         }
                     }

                     val webResourceResponse =
                         WebResourceResponse(mimeType, encoding, body?.byteStream());
                     var message = response?.message()

                     if (TextUtils.isEmpty(message) && code == 200) {
                         //message不能为空
                         message = "OK"
                     }

                     webResourceResponse.setStatusCodeAndReasonPhrase(code!!, message);
                     webResourceResponse.setResponseHeaders(map)
                     return webResourceResponse
                 }


                 return super.shouldInterceptRequest(view, request)*/

            }


        })



        mWebView?.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {

                pb_web.progress = newProgress
                if (newProgress >= 70) {
                    mWebView?.visibility = View.VISIBLE
                    pb_web.visibility = View.GONE
                }
                super.onProgressChanged(view, newProgress)
            }

        })


    }

    private fun getData() {
        startUrl = mUrl
    }

    private fun initUrl() {
        mWebView?.loadUrl(mUrl)
    }

    override fun onResume() {
        super.onResume()
        if (mWebView != null) {
            mWebView?.getSettings()?.setJavaScriptEnabled(true);
        }
    }

    override fun onStop() {
        super.onStop()
        //处理Android后台无法释放js导致发热耗电
        if (mWebView != null) {
            mWebView?.getSettings()?.setJavaScriptEnabled(false);
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView?.canGoBack()!!) {
                //当WebView不是处于第一页面时，返回上一个页面
                mWebView?.settings?.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
                mWebView?.goBack()
                return true
            } else {
                //当WebView处于第一页面时,直接退出
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        mWebviewRoot?.let { WebViewPool.instance?.removeWebView( mWebView) }
        super.onDestroy()
    }


}