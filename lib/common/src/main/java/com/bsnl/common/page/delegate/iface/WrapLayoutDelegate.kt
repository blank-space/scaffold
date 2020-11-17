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
interface WrapLayoutDelegate {

    fun setTitleLayoutLayout(@LayoutRes layoutResId: Int)

    fun setEmptyLayout(@LayoutRes layoutResId: Int)

    fun setLoadingLayout(@LayoutRes layoutResId: Int)

    fun setErrorLayout(@LayoutRes layoutResId: Int)

    fun setEmptyText(@StringRes text: Int)

    fun setEmptyText(text: String?)

    fun setEmptyTextColor(@ColorRes color: Int)

    fun setEmptyIcon(@DrawableRes icon: Int)

    fun setEmptyBtnTxt(text: String?)

    fun getEmptyView(): View?

    fun getErrorView(): View?

    fun getContentView(): View?

    fun getLoadingView(): View?

    fun getViewState(): ViewState?

    fun getRefreshLayout(): RefreshLayoutProxy?

    fun getTitleView(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): ITitleView?
}