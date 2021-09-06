package com.bsnl.sample.pkg.feature.view.viewpager

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bsnl.base.utils.showToast
import com.bsnl.common.iface.OnItemClickListener
import com.bsnl.common.page.base.LazyListFragment
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.utils.newFrgInstance
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.constant.Bundle_TITLE
import com.bsnl.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.TabViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 */
class TabFragment : LazyListFragment<TabViewModel>() {

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.register(PokemonItemViewBinder())
        (activity as ViewPagerActivity).getRvPool()?.let {
            getRecyclerView()?.setRecycledViewPool(it)
        }
        getRecyclerView()?.let {
            val lm = LinearLayoutManager(it.context)
            lm.recycleChildrenOnDetach = true
            it.layoutManager = lm
        }
    }


    override fun lazyInitListener() {
        val title = arguments?.getString(Bundle_TITLE, "1")
        RecyclerViewUtil.setOnItemClickListener(getRecyclerView(), object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                title?.showToast()
            }

            override fun onChildClick(v: View, position: Int) {
                title?.showToast()
            }
        }, R.id.name, R.id.avator)
    }

    companion object {
        fun newInstance(title: String) = newFrgInstance<TabFragment> {
            putString(Bundle_TITLE, title)
        }
    }


}