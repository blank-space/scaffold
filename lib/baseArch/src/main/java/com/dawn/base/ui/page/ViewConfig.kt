package com.dawn.base.ui.page.iface

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.dawn.base.R

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
data class ViewConfig(
    @LayoutRes
    var titleLayoutId: Int = R.layout.base_toolbar,
    @LayoutRes
    var loadingLayoutId: Int = R.layout.base_loading_layout,
    @LayoutRes
    var errorLayoutId: Int = R.layout.base_error_layout,
    @LayoutRes
    var emptyLayoutId: Int = R.layout.base_empty_layout,
    @DrawableRes
    var emptyDrawableId: Int = -1,
    @DrawableRes
    var errorDrawableId: Int = -1,
    var emptyTxt: String? = null,
    @ColorRes
    var emptyTxtColor: Int = 0,
    var emptyBtnTxt: String? = null,
    var illustrateTxt: String? = null,
    var retryBtnTxt: String? = null,
    @LayoutRes
    var bottomLayoutId: Int = -1,
    @LayoutRes
    var customLayoutId: Int = -1,
)