package com.dawn.launch.app

import android.graphics.Color
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.dawn.base.BaseApp
import com.dawn.base.databinding.BaseEmptyActivityBinding
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.doOnMainThreadIdle
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.pkg.feature.view.FirstFragment
import com.dawn.statusbar.*
import com.dawn.webview.view.WebViewPool


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */

class MainActivity : BaseActivity<MainViewModel, BaseEmptyActivityBinding>() {

    @Autowired
    @JvmField
    var sampleService: ISampleService? = null


    lateinit var fragment: FirstFragment

    private val contentView by lazy { MainLayout(this, sampleService) }

    override fun initView() {
        fragment = FirstFragment()

        doOnMainThreadIdle({
            WebViewPool.init(BaseApp.application)
        }, 10)

    }

    override fun customStatusBar() {
        fitStatusBar(false)
        setStatusBarColor(Color.parseColor("#00000000"))
        setLightStatusBar(true)
    }

    override fun getLayout(): View {
        return contentView
    }

    override fun isNeedInjectARouter() = true

}

