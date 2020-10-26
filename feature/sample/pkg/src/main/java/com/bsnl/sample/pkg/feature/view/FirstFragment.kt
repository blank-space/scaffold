package com.bsnl.sample.pkg.feature.view

import android.view.View
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.dataBinding.DataBindingFragment
import com.bsnl.common.utils.getVm
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.feature_sample_pkg_fragment_first.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class FirstFragment : DataBindingFragment<TestViewModel>() {



    override fun initView() {

    }

    override fun initData() {
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_fragment_first

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? {
        return null
    }

    override fun initViewModel(): TestViewModel = getVm()

    override fun initListener() {
        super.initListener()

    }
}