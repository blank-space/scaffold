package com.bsnl.common.iface

import android.view.View


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface IViewState {
    fun setState(state: ViewStateWithMsg)

    fun onPageReload(v: View?) {}

    fun processNoDataBtnClick(v: View?) {}

    fun processRefresh(refreshLayout: IRefreshLayout?) {}

    fun processLoadMore(refreshLayout: IRefreshLayout?) {}
}