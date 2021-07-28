package com.bsnl.sample.pkg.feature.view

import android.view.View
import com.bsnl.common.page.base.BaseFragment
import com.bsnl.common.utils.getVm
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.viewmodel.TestViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class FirstFragment : BaseFragment<TestViewModel>() {



    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_fragment_first

    override fun initViewModel(): TestViewModel = getVm()

    override fun initView(view: View) {}

    override fun getLayout(): View? = null

}