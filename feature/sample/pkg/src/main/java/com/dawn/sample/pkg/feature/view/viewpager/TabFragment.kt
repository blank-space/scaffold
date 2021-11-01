package com.dawn.sample.pkg.feature.view.viewpager

import androidx.recyclerview.widget.LinearLayoutManager
import com.dawn.base.ui.page.base.SimpleLazyListFragment
import com.dawn.base.utils.newFrgInstance
import com.dawn.sample.pkg.feature.constant.Bundle_TITLE
import com.dawn.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.dawn.sample.pkg.feature.viewmodel.TabViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 */
class TabFragment : SimpleLazyListFragment<TabViewModel>() {

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
    }

    companion object {
        fun newInstance(title: String) = newFrgInstance<TabFragment> {
            putString(Bundle_TITLE, title)
        }
    }
}