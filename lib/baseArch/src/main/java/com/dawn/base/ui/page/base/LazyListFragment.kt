package com.dawn.base.ui.page.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.dawn.base.ui.page.delegate.iface.PageType
import com.dawn.base.viewmodel.base.BaseListViewModel


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 懒加载列表
 */
abstract class LazyListFragment<T : BaseListViewModel, VB : ViewBinding> : BaseListFragment<T,VB>() {
    protected var mHasInit = false


    override fun initData() {}

    override fun initView(v: View) {}

    override fun onResume() {
        super.onResume()
        if (!mHasInit) {
            mHasInit = true
            setupLiveDataCallback()
            lazyInitListener()
            if (mViewModel.providerData().isNullOrEmpty()) {
                fetchData()
            }
        }
    }

    override fun getPageType(): Int {
        return PageType.LIST
    }

    protected open fun lazyInitListener() {}

    override fun onDestroyView() {
        mHasInit = false
        super.onDestroyView()
    }

}