package com.dawn.launch.app

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.dawn.base.BaseApp
import com.dawn.base.databinding.BaseEmptyLayoutBinding
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.ui.page.base.BaseBindingActivity
import com.dawn.base.utils.doOnMainThreadIdle
import com.dawn.base.widget.webview.WebViewPool
import com.dawn.sample.export.api.ISampleService

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
        GlobalAsyncHandler.postDelayed(1000) {
            setState(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
        }
        doOnMainThreadIdle({
            WebViewPool.init(BaseApp.application)
        },10)
    }

    override fun initStatusBar() {}

    override fun getLayout(): View {
        return contentView
    }

    override fun isNeedInjectARouter() = true

    override fun initData() {

    }
}