package com.bsnl.common.iface

import android.view.View

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface OnViewStateListener {

    fun onReload(v: View?)

    fun onNoDataBtnClick(v: View?)

    fun onRefresh(refreshLayout: IRefreshLayout?)

    fun onLoadMore(refreshLayout: IRefreshLayout?)
}