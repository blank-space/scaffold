package com.dawn.launch.app

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.dawn.base.BaseApp
import com.dawn.base.databinding.BaseEmptyActivityBinding
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.doOnMainThreadIdle
import com.dawn.base.widget.webview.WebViewPool
import com.dawn.sample.export.api.ISampleService

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class MainActivity : BaseActivity<MainViewModel, BaseEmptyActivityBinding>() {

    @Autowired
    @JvmField
    var sampleService: ISampleService? = null

    private val contentView by lazy { MainLayout(this, sampleService) }

    override fun initView() {
        getLayoutDelegateImpl()?.delayToChangeLoadingViewToSuccess = 300
        doOnMainThreadIdle({
            WebViewPool.init(BaseApp.application)
        }, 10)


    }

    override fun initStatusBar() {}

    override fun getLayout(): View {
        return contentView
    }

    override fun isNeedInjectARouter() = true


    fun login() {
        mViewModel.login().observe(this) {
            it.let {
                L.e("登陆成功,$it")
            }
        }
    }
}

