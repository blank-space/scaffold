package com.bsnl.sample.pkg.feature.view.viewpager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.bsnl.common.page.base.BaseBindingActivity
import com.bsnl.common.utils.startActivity
import com.bsnl.common.viewmodel.StubViewModel
import com.bsnl.sample.pkg.databinding.FeatureSamplePkgActivityViewpagerBinding
import com.bsnl.sample.pkg.feature.view.FirstFragment
import com.drakeet.multitype.MultiTypeAdapter
import com.google.android.material.tabs.TabLayoutMediator


/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   : ViewPager+Fragment+RecyclerView+RecyclerViewPool的示例
 */
class ViewPagerActivity : BaseBindingActivity<StubViewModel, FeatureSamplePkgActivityViewpagerBinding>() {
    private val tabs = listOf("tab1", "tab2", "tab3")


    private var recycledViewPool: RecycledViewPool? = RecycledViewPool()
    private val fragments= arrayListOf<Fragment>(TabFragment.newInstance(""),FirstFragment(),FirstFragment())

    private val tabAdapter by lazy {
        TabAdapter(this,fragments)
    }

    private  val mAdapter = MultiTypeAdapter(tabs)

    /**
     * MultiTypeAdapter没提供设置viewType的api,viewType等于该VH在的添加到集合时候的位置(index),所以这边写 "0"
     *
     * 备注:不建议把RecycledViewPool放置在ViewModel中提供给V层，ViewModel不应该持有跟Context引用，避免内存泄漏
     */
    fun getRvPool(): RecycledViewPool? {
        if (recycledViewPool == null) {
            recycledViewPool = RecycledViewPool()
            recycledViewPool!!.setMaxRecycledViews(0, 8)
        }
        return recycledViewPool
    }

    override fun isUseDefaultLoadService(): Boolean {
        return false
    }

    override fun initView() {
        getTitleView()?.setTitleText(TAG)

        /** 一般View的写法，跟使用RecyclerView一致 */
        //mAdapter.register(StringItemViewBinder())

        binding.viewPager2.adapter = tabAdapter
        binding.viewPager2.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = "Tab ${(position + 1)}"
        }.attach()
    }


    override fun initData() {}

    companion object {
        fun startAction(context: Context) {
            startActivity<ViewPagerActivity>(context)
        }
    }

}