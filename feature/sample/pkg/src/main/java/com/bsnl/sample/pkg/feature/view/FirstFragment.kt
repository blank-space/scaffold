package com.bsnl.sample.pkg.feature.view

import android.view.View
import com.bsnl.common.page.base.BaseBindingFragment
import com.bsnl.sample.pkg.databinding.FeatureSamplePkgFragmentFirstBinding
import com.bsnl.sample.pkg.feature.viewmodel.TestViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class FirstFragment : BaseBindingFragment<TestViewModel,FeatureSamplePkgFragmentFirstBinding>() {

    /**
     * 在fragment中，LifecycleOwner不能使用this,而是viewLifecycleOwner。
     * 由于 Fragment 的 Lifecycle 与 Fragment#mView 的 Lifecycle 不一致导致我们订阅 LiveData
     * 的时机和所使用的 LivecycleOwner 不匹配，所以在任何基于 replace 进行页面切换的场景中，
     * 例如 ViewPager、Navigation 等会发生上述bug。
     *
     * 详细见 @see https://mp.weixin.qq.com/s/_2YSV_JsjDJ7CuHJngMbqQ
     */
    override fun initData() {
        mViewModel.count.observe(viewLifecycleOwner) {

        }
    }


    override fun initView(view: View) {}


}