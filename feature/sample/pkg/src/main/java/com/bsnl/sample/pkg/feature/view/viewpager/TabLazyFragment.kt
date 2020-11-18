package com.bsnl.sample.pkg.feature.view.viewpager

import com.bsnl.base.log.L
import com.bsnl.common.utils.getVm

import com.bsnl.common.utils.newFrgInstance
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.constant.Bundle_TITLE
import com.bsnl.sample.pkg.feature.viewmodel.TabViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/12
 * @desc   : 懒加载页面
 */
class TabLazyFragment : com.bsnl.databinding.LazyDataBindingFragment<TabViewModel>() {
    override fun lazyInitView() {
        L.d("lazyInitView")
    }

    override fun lazyInitData() {
        L.d("lazyInitData:${arguments?.getString(Bundle_TITLE)}")
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_fragment_first

    override fun initBindingConfig(layoutId: Int): com.bsnl.databinding.DataBindingConfig? = null

    override fun initViewModel(): TabViewModel = getVm()

    companion object {
        fun newInstance(title: String) = newFrgInstance<TabLazyFragment> {
            putString(Bundle_TITLE, title)
        }
    }
}