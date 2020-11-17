package com.bsnl.common.iface

import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface IRefreshLayout {
    fun setRefreshAndLoadMoreListener(listener: OnRefreshAndLoadMoreListener?): IRefreshLayout?
    fun setEnableRefresh(enable: Boolean): IRefreshLayout?
    fun setEnableLoadMore(enable: Boolean): IRefreshLayout?
    fun finishRefresh()
    fun finishLoadMore()
    fun setNoMoreData(boolean: Boolean)
    fun autoRefresh()
    fun getSmartRefreshLayout():SmartRefreshLayout?
}