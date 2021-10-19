package com.bsnl.common.page.base

import android.view.View
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.IViewState
import com.bsnl.common.page.delegate.iface.OnViewStateListener

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/19
 * @desc   :
 */
class PageStateChangeListener(val viewState: IViewState) : OnViewStateListener {

    override fun onReload(v: View?) {
        viewState.onPageReload(v)
    }

    override fun onNoDataBtnClick(v: View?) {
        viewState.processNoDataBtnClick(v)
    }

    override fun onRefresh(refreshLayout: IRefreshLayout?) {
        viewState.processRefresh(refreshLayout)
    }

    override fun onLoadMore(refreshLayout: IRefreshLayout?) {
        viewState.processLoadMore(refreshLayout)
    }
}