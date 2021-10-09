package com.bsnl.common.page.delegate.iface

import android.view.View
import com.bsnl.common.iface.IRefreshLayout

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