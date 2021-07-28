package com.bsnl.launch.app

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.common.page.base.BaseActivity
import com.bsnl.common.utils.getVm
import com.bsnl.sample.export.api.ISampleService

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class MainActivity : BaseActivity<MainViewModel>() {

    @Autowired
    @JvmField
    var sampleService: ISampleService? = null

    private val contentView by lazy { MainLayout(this,sampleService) }

    override fun initView() {

    }

    override fun initStatusBar() {}

    override fun getLayout(): View {
        return contentView
    }

    override fun initViewModel(): MainViewModel = getVm()

    override fun initData() {
    }

    override fun getLayoutId(): Int = 0


}