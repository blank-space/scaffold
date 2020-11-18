package com.bsnl.sample.pkg.feature.view

import com.bsnl.databinding.DataBindingConfig
import com.bsnl.databinding.DataBindingFragment
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

    override fun initView() {
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_fragment_first


    override fun initViewModel(): TestViewModel = getVm()


}