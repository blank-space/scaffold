package com.bsnl.sample.pkg.feature.view.webview

import android.os.Bundle
import android.view.View
import com.bsnl.base.log.L
import com.bsnl.base.utils.showToast
import com.bsnl.common.dataBinding.BaseListDataBindingFragment
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.iface.OnItemClickListener
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.utils.getVm
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.constant.Bundle_TITLE
import com.bsnl.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.bsnl.sample.pkg.feature.view.viewpager.ViewPagerActivity
import com.bsnl.sample.pkg.feature.viewmodel.TabViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   :
 */
class TabFragment : BaseListDataBindingFragment<TabViewModel>() {

    override fun registerItem(adapter: MultiTypeAdapter) {
        //优化内存和渲染
        (activity as ViewPagerActivity).getRvPool()?.let {
            L.d("setRecycledViewPool")
            mRecyclerView?.setRecycledViewPool(it)
        }
        adapter.register(PokemonItemViewBinder(arguments?.getString(Bundle_TITLE,"1")!!))
        adapter.setHasStableIds(true)

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        if(mViewModel.providerData().isNullOrEmpty()) {
            mRefreshLayout?.autoRefresh()
        }
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_fragment_tab

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? = null

    override fun initViewModel(): TabViewModel = getVm()

    override fun initListener() {
        super.initListener()
        RecyclerViewUtil.setOnItemClickListener(mRecyclerView, object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                v.id.toString().showToast()
            }

            override fun onChildClick(v: View, position: Int) {
                v.id.toString().showToast()

            }
        }, R.id.name, R.id.avator)
    }

    companion object {
        fun newInstance(title: String): TabFragment {
            val args = Bundle().apply {
                putString(Bundle_TITLE, title)
            }
            val fragment = TabFragment()
            fragment.arguments = args
            return fragment
        }
    }

}