package com.bsnl.launch.app

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.bsnl.common.databinding.LibCommonEmptyLayoutBinding
import com.bsnl.common.page.base.BaseBindingActivity
import com.bsnl.sample.export.api.ISampleService

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class MainActivity : BaseBindingActivity<MainViewModel, LibCommonEmptyLayoutBinding>() {

    @Autowired
    @JvmField
    var sampleService: ISampleService? = null

    private val contentView by lazy { MainLayout(this, sampleService) }

    override fun initView() {

    }

    override fun initStatusBar() {}

    override fun getLayout(): View {
        return contentView
    }

    override fun isNeedInjectARouter() = true
    override fun initData() {

    }
}