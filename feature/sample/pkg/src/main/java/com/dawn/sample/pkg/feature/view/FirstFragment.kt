package com.dawn.sample.pkg.feature.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.bsnl.constraint.export.api.IConstraintService
import com.dawn.base.log.L
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.ui.page.base.BaseFragment
import com.dawn.base.utils.arguments
import com.dawn.base.utils.getApplicationScopeViewModel
import com.dawn.base.utils.onClick
import com.dawn.base.utils.withArguments
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.pkg.databinding.FeatureSamplePkgFragmentFirstBinding
import com.dawn.sample.pkg.feature.constant.BUNDLE_INDEX
import com.dawn.sample.pkg.feature.constant.BUNDLE_TITLE
import com.dawn.sample.pkg.feature.domain.message.ShareViewModel
import com.dawn.sample.pkg.feature.view.download.DownLoadActivity
import com.dawn.sample.pkg.feature.view.viewpager.TabFragment
import com.dawn.sample.pkg.feature.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
@AndroidEntryPoint
class FirstFragment : BaseFragment<TestViewModel, FeatureSamplePkgFragmentFirstBinding>() {
    private var testEventObserver: Observer<Int>? = null
    private val event: ShareViewModel by lazy {
        getApplicationScopeViewModel()
    }
    private val actionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val code = it.resultCode
            val data = it.data?.getStringExtra("data")
            L.d("code:$code,data:$data")
        }

    @Autowired
    @JvmField
    var sampleService: IConstraintService? = null
    //共享Activity的ViewModel：activityViewModels
    private val vm : TestViewModel by activityViewModels()

    /**
     * 在fragment中，LifecycleOwner不能使用this,而是viewLifecycleOwner。
     * 由于 Fragment 的 Lifecycle 与 Fragment#mView 的 Lifecycle 不一致导致我们订阅 LiveData
     * 的时机和所使用的 LivecycleOwner 不匹配，所以在任何基于 replace 进行页面切换的场景中，
     * 例如 ViewPager、Navigation 等会发生上述bug。
     *
     * 详细见 @see https://mp.weixin.qq.com/s/_2YSV_JsjDJ7CuHJngMbqQ
     */
    override fun initData() {
        event.countLiveData.observeForever(Observer<Int> {
            L.d("fragment 接受数据:$it")
        }.apply {
            testEventObserver = this
        })
    }

    //将args传到savedStateHandle。 注：单独使用时生效，与activity共享viewModel时无效
   /* override fun setArguments(args: Bundle?) {
        if (args != null) {
            super.setArguments(Bundle(args))
        } else {
            super.setArguments(null)
        }
    }*/

    override fun initViewModel(): TestViewModel {
        return vm
    }

    override fun initView(view: View) {
        binding.tv.text = "count:${mViewModel.getCount()}"
    }

    override fun initListener() {
        super.initListener()
        binding.tv.onClick = {
            mViewModel.add()
            binding.tv.text = "count:${mViewModel.getCount()}"
           // context?.let { actionLauncher.launch(Intent(it, DownLoadActivity::class.java)) }
        }
        binding.tvVisit.onClick = {
            sampleService?.startBarrierActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (testEventObserver != null) {
            event.countLiveData.removeObserver(testEventObserver!!)
        }
    }

    override fun isNeedInjectARouter() = true

    companion object {
        fun newInstance(title: String, pageIndex: Int) = FirstFragment().withArguments(
            BUNDLE_TITLE to title,
            BUNDLE_INDEX to pageIndex
        )
    }
}