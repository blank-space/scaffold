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

    /**
     * 自定义Title
     */
    fun setTitleLayoutLayout(@LayoutRes layoutResId: Int)

    /**
     * 自定义空页面
     */
    fun setEmptyLayout(@LayoutRes layoutResId: Int)

    /**
     * 自定义Loading页面
     */
    fun setLoadingLayout(@LayoutRes layoutResId: Int)

    /**
     * 自定义错误页面
     */
    fun setErrorLayout(@LayoutRes layoutResId: Int)

    fun setEmptyText(@StringRes text: Int)

    fun setEmptyText(text: String?)

    fun setEmptyTextColor(@ColorRes color: Int)

    /**
     * 自定义空状态的图标
     */
    fun setEmptyIcon(@DrawableRes icon: Int)

    /**
     * 自定义错误状态的图标
     */
    fun setErrorIcon(@DrawableRes icon: Int)

    fun setEmptyBtnTxt(text: String?)

    fun getEmptyView(): View?

    fun getErrorView(): View?

    fun getContentView(): View?

    fun getLoadingView(): View?

    fun getViewState(): ViewState?

    fun getRefreshLayout(): RefreshLayoutProxy?

    fun getTitleView(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): ITitleView?

    /**
     *自定义具体说明文案
     */
    fun setIllustrateText(@StringRes text: Int)

    /**
     * 自定义具体说明文案
     */
    fun setIllustrateText(text: String?)

    /**
     * 自定义交互按钮文字
     */
    fun setRetryText(text: String?)

    fun setRetryText(@StringRes text: Int)

    /**
     * 自定义底部界面
     */
    fun setBottomLayout(@LayoutRes layoutResId: Int, height:Int)

    fun getBottomLayout():View?

    /**
     * 完全自定义界面
     */
    fun setCustomLayout(@LayoutRes layoutResId: Int)
}