package com.bsnl.common.iface

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
data class ViewConfig(
    @LayoutRes
    var titleLayoutId: Int = -1,
    @LayoutRes
    var loadingLayoutId: Int = -1,
    @LayoutRes
    var errorLayoutId: Int = -1,
    @LayoutRes
    var emptyLayoutId: Int = -1,
    @DrawableRes
    var emptyDrawableId: Int = -1,
    var emptyTxt: String? = null,
    @ColorRes
    var emptyTxtColor: Int = 0,
    var emptyBtnTxt: String? = null
)