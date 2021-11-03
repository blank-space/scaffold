package com.dawn.sample.pkg.feature.view

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.dawn.base.log.L
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.ui.page.base.BaseFragment
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
class FirstFragment : BaseFragment<TestViewModel, FeatureSamplePkgFragmentFirstBinding>() {
    private var testEventObserver: Observer<Int>? = null
    private val event: ShareViewModel by lazy {
        mActivity?.get()?.getApplicationScopeViewModel()!!
    }
    private val actionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val code = it.resultCode
            val data = it.data?.getStringExtra("data")
            L.d("code:$code,data:$data")
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
        setState(ViewStateWithMsg(state = ViewState.STATE_LOADING))
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
            context?.let { actionLauncher.launch(Intent(it, DownLoadActivity::class.java)) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (testEventObserver != null) {
            event.countLiveData.removeObserver(testEventObserver!!)
        }
    }

}