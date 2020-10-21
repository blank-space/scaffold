package com.bsnl.common.viewmodel

import androidx.annotation.IntDef

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   : 请求类型
 */
open interface RequestType {
    @IntDef(value = [INIT, REFRESH, LOAD_MORE])
    annotation class Val
    companion object {
        /**
         * 初始化
         */
        const val INIT = 1

        /**
         * 刷新
         */
        const val REFRESH = 2

        /**
         * 加载更多
         */
        const val LOAD_MORE = 3
    }
}