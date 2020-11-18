package com.bsnl.sample.pkg.feature.view.viewpager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.bsnl.databinding.DataBindingActivity
import com.bsnl.databinding.DataBindingConfig
import com.bsnl.common.page.base.BaseActivity
import com.bsnl.common.ui.viewpager.CustomerViewpager
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.common.viewmodel.StubViewModel
import com.bsnl.sample.pkg.R
import kotlinx.android.synthetic.main.feature_sample_pkg_activity_viewpager.*


/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   : ViewPager+Fragment+RecyclerView+RecyclerViewPool的示例
 */
class ViewPagerActivity : BaseActivity<StubViewModel>() {
    private val tabs = arrayOf("tab1", "tab2", "tab3")

    private var recycledViewPool: RecycledViewPool? =  RecycledViewPool()

    /**
     * MultiTypeAdapter没提供设置viewType的api,viewType等于该VH在的添加到集合时候的位置(index),所以这边写 "0"
     *
     * 备注:不建议把RecycledViewPool放置在ViewModel中提供给V层，ViewModel不应该持有跟Context引用，避免内存泄漏
     */
    fun getRvPool(): RecycledViewPool? {
        if(recycledViewPool==null) {
            recycledViewPool = RecycledViewPool()
            recycledViewPool!!.setMaxRecycledViews(0, 8)
        }
        return recycledViewPool
    }


    override fun initView() {
    getTitleView()?.setTitleText(TAG)

        //添加tab
        for (i in 0 until tabs.size) {
            tab_layout.addTab(tab_layout.newTab().setText(tabs[i]))
        }

        //设置Title和创建Fragment
        view_pager.setInitFragmentListener(object : CustomerViewpager.OnInitFragmentListener {
            override fun getFragment(position: Int): Fragment {
                return TabFragment.newInstance("Tab_${position}")
            }
            override fun getTabs(): Array<String> = tabs
        }, supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        //设置TabLayout和ViewPager联动
        tab_layout.setupWithViewPager(view_pager, false)
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_activity_viewpager

   // override fun initBindingConfig(layoutId: Int): DataBindingConfig? = null

    override fun initViewModel(): StubViewModel = getVm()

    override fun initData() {
    }

    companion object {
        fun startAction(context: Context) {
            startActivity<ViewPagerActivity>(context)
        }
    }
}