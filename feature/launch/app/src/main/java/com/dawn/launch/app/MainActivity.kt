package com.dawn.launch.app

import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.dawn.base.BaseApp
import com.dawn.base.databinding.BaseEmptyLayoutBinding
import com.dawn.base.log.L
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.ui.page.base.BaseBindingActivity
import com.dawn.base.utils.doOnMainThreadIdle
import com.dawn.base.utils.getApplicationScopeViewModel
import com.dawn.base.widget.webview.WebViewPool
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.pkg.feature.domain.message.ShareViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class MainActivity : BaseBindingActivity<MainViewModel, BaseEmptyLayoutBinding>() {

    @Autowired
    @JvmField
    var sampleService: ISampleService? = null

    private val contentView by lazy { MainLayout(this, sampleService) }

    override fun initView() {
        //计算冷启动
        reportFullyDrawn()
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

    override fun initData() {
        setState(ViewStateWithMsg(state = ViewState.STATE_LOADING))
    }
}