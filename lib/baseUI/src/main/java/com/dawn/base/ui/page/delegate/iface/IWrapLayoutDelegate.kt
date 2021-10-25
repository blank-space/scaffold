package com.dawn.base.ui.page.delegate.iface

import android.view.View
import androidx.annotation.LayoutRes
import com.dawn.base.ui.page.iface.ITitleView
import com.dawn.base.ui.page.iface.ViewState
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface IWrapLayoutDelegate {


    fun setTitleLayoutLayout(@LayoutRes layoutResId: Int)

    fun getContentView(): View?

    fun getViewState(): ViewState?

    fun getRefreshLayout(): SmartRefreshLayout?

    fun getTitleView(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): ITitleView?


}