package com.bsnl.common.iface

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.widget.Toolbar

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface ITitleView {

    fun setBackgroundColor(@ColorInt color: Int)
    fun setVisibility(visibility: Int)
    fun setImmersionBarEnable(isImmersionBarEnable: Boolean)

    fun setNavIconOnClickListener(onClickListener: View.OnClickListener?): ITitleView?
    fun setNavIcon(@Nullable icon: Drawable?): ITitleView?
    fun setNavIcon(@DrawableRes resId: Int): ITitleView?
    fun setNavIconVisibility(isVisible: Boolean): ITitleView?
    fun setLeftText(
        leftTxt: String?,
        onClickListener: View.OnClickListener?
    ): ITitleView?

    fun setLeftText(leftTxt: String?): ITitleView?
    fun setLeftTextColor(@ColorInt color: Int): ITitleView?
    fun setLeftTextSize(sizeOfSp: Int): ITitleView?
    fun getLeftText(): TextView?

    fun setTitleText(title: CharSequence?): ITitleView?
    fun setTitleText(@StringRes title: Int): ITitleView?
    fun setTitleTextColor(@ColorInt color: Int): ITitleView?
    fun setTitleTextSize(sizeOfSp: Int): ITitleView?
    fun setTitleGravityLeft(): ITitleView?
    fun getTitleText(): TextView?
    fun getToolbar(): Toolbar?

    fun getView(): View?

    fun <T : View?> setCenterView(@LayoutRes layoutId: Int): T?
    fun <T : View?> setLayoutView(@LayoutRes layoutId: Int): T?

    fun addMenuItem(
        view: View?,
        onClickListener: View.OnClickListener?
    ): ITitleView?

    fun addMenuItem(viewGroup: ViewGroup?): ITitleView?

    fun addMenuItem(
        text: String?,
        onClickListener: View.OnClickListener?
    ): TextView?

    fun clearMenuItems()

}