package com.bsnl.sample.pkg.feature.view.viewpager

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsnl.base.utils.showToast
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.dataBinding.LazyListDataBindingFragment
import com.bsnl.common.iface.OnItemClickListener
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.newFrgInstance
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.constant.Bundle_TITLE
import com.bsnl.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.TabViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   : 使用RecycledViewPool优化内存和渲染速度
 * /验证如下：每个tab滑动一下，出现加载更多，然后切换tab再滑动，直到最后一个tab，使用pool时，创建了13个MyHolder，不使用pool，创建了21个MyHolder
 */
class TabFragment : LazyListDataBindingFragment<TabViewModel>() {

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.register(PokemonItemViewBinder(arguments?.getString(Bundle_TITLE, "1")!!))

        (activity as ViewPagerActivity).getRvPool()?.let {
            getRecyclerView()?.setRecycledViewPool(it)
        }

        getRecyclerView()?.let {
            val lm = LinearLayoutManager(it.context)
            lm.recycleChildrenOnDetach = true
            it.layoutManager = lm

        }
    }
    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_fragment_tab

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? = null

    override fun initViewModel(): TabViewModel = getVm()

    override fun initListener() {
        super.initListener()
        RecyclerViewUtil.setOnItemClickListener(getRecyclerView(), object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                v.id.toString().showToast()
            }

            override fun onChildClick(v: View, position: Int) {
                v.id.toString().showToast()

            }
        }, R.id.name, R.id.avator)
    }

    companion object {
        fun newInstance(title: String) = newFrgInstance<TabFragment> {
            putString(Bundle_TITLE, title)
        }
    }

}