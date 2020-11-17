package com.bsnl.common.iface

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.bsnl.common.R

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
data class ViewConfig(
    @LayoutRes
    var titleLayoutId: Int = R.layout.lib_common_base_toolbar,
    @LayoutRes
    var loadingLayoutId: Int = R.layout.lib_common_stub_progress_wait,
    @LayoutRes
    var errorLayoutId: Int = R.layout.lib_common_stub_reload_layout,
    @LayoutRes
    var emptyLayoutId: Int = R.layout.lib_common_stub_no_data,
    @DrawableRes
    var emptyDrawableId: Int = -1,
    var emptyTxt: String? = null,
    @ColorRes
    var emptyTxtColor: Int = 0,
    var emptyBtnTxt: String? = null
)