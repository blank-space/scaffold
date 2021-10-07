package com.bsnl.common.page.delegate.iface

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.ITitleView
import com.bsnl.common.iface.ViewState
import com.bsnl.common.refreshLayout.RefreshLayoutProxy

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface IWrapLayoutDelegate {


    fun setTitleLayoutLayout(@LayoutRes layoutResId: Int)

    fun getContentView(): View?

    fun getViewState(): ViewState?

    fun getRefreshLayout(): RefreshLayoutProxy?

    fun getTitleView(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): ITitleView?


}