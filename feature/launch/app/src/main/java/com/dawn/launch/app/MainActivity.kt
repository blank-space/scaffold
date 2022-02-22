package com.dawn.launch.app

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.dawn.base.BaseApp
import com.dawn.base.databinding.BaseEmptyActivityBinding
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.utils.doOnMainThreadIdle
import com.dawn.base.widget.webview.WebViewPool
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.pkg.feature.view.FirstFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */

@AndroidEntryPoint
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

