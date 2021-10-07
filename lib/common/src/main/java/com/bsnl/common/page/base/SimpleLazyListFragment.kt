package com.bsnl.common.page.base

import android.view.View
import com.bsnl.common.viewmodel.BaseListViewModel


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 懒加载列表
 */
abstract class SimpleLazyListFragment<T : BaseListViewModel> : SimpleListFragment<T>() {
    protected var mHasInit = false


    override fun initData() {}

    override fun initView(v: View) {}

    override fun onResume() {
        super.onResume()
        if (!mHasInit) {
            mHasInit = true
            setupListViewDelegate()
            setupLiveDataCallback()
            lazyInitListener()
            if (mViewModel.providerData().isNullOrEmpty()) {
                fetchData()
            }
        }
    }


    protected open fun lazyInitListener() {}

    override fun onDestroyView() {
        mHasInit = false
        super.onDestroyView()
    }

}