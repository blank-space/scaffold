package com.dawn.base.ui.page.delegate.iface

import androidx.annotation.IntDef

/**
 * @author : LeeZhaoXing
 * @date   : 2022/1/20
 * @desc   :
 */
interface PageType {
    @IntDef(value = [NORMAL, LIST])
    annotation class Val
    companion object {
        const val NORMAL = 1
        const val LIST = 2

    }
}