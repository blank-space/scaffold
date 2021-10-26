package com.dawn.sample.pkg.feature.view

import android.view.View
import androidx.lifecycle.Observer
import com.dawn.base.log.L
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.ui.page.base.BaseBindingFragment
import com.dawn.base.utils.getApplicationScopeViewModel
import com.dawn.base.utils.onClick
import com.dawn.sample.pkg.databinding.FeatureSamplePkgFragmentFirstBinding
import com.dawn.sample.pkg.feature.domain.message.ShareViewModel
import com.dawn.sample.pkg.feature.view.download.DownLoadActivity
import com.dawn.sample.pkg.feature.viewmodel.TestViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class FirstFragment : BaseBindingFragment<TestViewModel, FeatureSamplePkgFragmentFirstBinding>() {
    private var testEventObserver: Observer<Int>? = null
    private val event: ShareViewModel by lazy {
        mActivity?.get()?.getApplicationScopeViewModel()!!
    }

    /**
     * 在fragment中，LifecycleOwner不能使用this,而是viewLifecycleOwner。
     * 由于 Fragment 的 Lifecycle 与 Fragment#mView 的 Lifecycle 不一致导致我们订阅 LiveData
     * 的时机和所使用的 LivecycleOwner 不匹配，所以在任何基于 replace 进行页面切换的场景中，
     * 例如 ViewPager、Navigation 等会发生上述bug。
     *
     * 详细见 @see https://mp.weixin.qq.com/s/_2YSV_JsjDJ7CuHJngMbqQ
     */
    override fun initData() {
        GlobalAsyncHandler.postDelayed(1000) {
            setState(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
        }
        event.countLiveData.observeForever(Observer<Int> {
            L.d("event value:$it")
        }.apply {
            testEventObserver = this
        })
    }

    override fun initView(view: View) {}

    override fun initListener() {
        super.initListener()
        binding.tv.onClick = {
            context?.let { DownLoadActivity.startAction(it) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (testEventObserver != null) {
            event.countLiveData.removeObserver(testEventObserver!!)
        }
    }

}