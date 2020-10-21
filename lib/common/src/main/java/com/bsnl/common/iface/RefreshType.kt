package com.bsnl.common.iface

import androidx.annotation.IntDef

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   : 刷新控件类型
 */
open interface RefreshType {
    @IntDef(value = [NONE, REFRESH_AND_LOAD_MORE, REFRESH_ONLY, LOAD_MORE_ONLY])
    annotation class Val
    companion object {
        /**
         * 无刷新控件
         */
        const val NONE = 1

        /**
         * 刷新与加载更多
         */
        const val REFRESH_AND_LOAD_MORE = 2

        /**
         * 只有刷新
         */
        const val REFRESH_ONLY = 3

        /**
         * 只有加载更多
         */
        const val LOAD_MORE_ONLY = 4
    }
}