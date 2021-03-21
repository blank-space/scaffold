package com.bsnl.launch.app

import android.view.View
import com.bsnl.common.iface.ViewState
import com.bsnl.common.page.base.BaseActivity
import com.bsnl.common.utils.getVm
import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.ViewHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel>() {

    @Inject
    lateinit var viewHelper: ViewHelper

    private val contentView by lazy { MainLayout(this) }

    override fun initView() {}

    override fun initStatusBar() {}

    override fun getLayout(): View? {
        return contentView
    }

    override fun initViewModel(): MainViewModel = getVm()

    override fun initData() {
        setState(ViewState.STATE_LOADING)

    }

    override fun getLayoutId(): Int = 0


}