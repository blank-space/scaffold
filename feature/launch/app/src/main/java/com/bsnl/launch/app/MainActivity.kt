package com.bsnl.launch.app

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.bsnl.base.log.L
import com.bsnl.base.utils.GlobalAsyncHandler
import com.bsnl.common.databinding.CommonEmptyLayoutBinding
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.page.base.BaseBindingActivity
import com.bsnl.sample.export.api.ISampleService

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class MainActivity : BaseBindingActivity<MainViewModel, CommonEmptyLayoutBinding>() {

    @Autowired
    @JvmField
    var sampleService: ISampleService? = null

    private val contentView by lazy { MainLayout(this, sampleService) }

    override fun initView() {
        GlobalAsyncHandler.postDelayed(1000) {
            setState(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
        }
    }

    override fun initStatusBar() {}

    override fun getLayout(): View {
        return contentView
    }

    override fun isNeedInjectARouter() = true

    override fun initData() {

    }
}