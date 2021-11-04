package com.dawn.base.ui.page.iface

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

    /**
     * 动态修改状态页(loadSir#CallBack),默认实现ErrorLayoutCallback，如果其他需求，子类复写
     */
    fun modifyTheCallbackDynamically(msg:String?)
}