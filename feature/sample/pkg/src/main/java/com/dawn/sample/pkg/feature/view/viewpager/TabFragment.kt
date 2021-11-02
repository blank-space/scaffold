package com.dawn.sample.pkg.feature.view.viewpager

import androidx.recyclerview.widget.LinearLayoutManager
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.SimpleLazyListFragment
import com.dawn.base.utils.arguments
import com.dawn.base.utils.intentExtras
import com.dawn.base.utils.withArguments
import com.dawn.sample.pkg.feature.constant.BUNDLE_INDEX
import com.dawn.sample.pkg.feature.constant.BUNDLE_TITLE
import com.dawn.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.dawn.sample.pkg.feature.viewmodel.TabViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 */
class TabFragment : SimpleLazyListFragment<TabViewModel>() {
    private val title: String? by arguments(BUNDLE_TITLE)
    private val index by arguments(BUNDLE_INDEX, 12)

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

    override fun initData() {
        super.initData()
        L.e("title：$title,index:$index")
    }

    override fun lazyInitListener() {
    }

    companion object {
        fun newInstance(title: String, pageIndex: Int) = TabFragment().withArguments(
            BUNDLE_TITLE to title,
            BUNDLE_INDEX to pageIndex
        )
    }
}


