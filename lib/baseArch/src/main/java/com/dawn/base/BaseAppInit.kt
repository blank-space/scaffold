package com.dawn.base

import android.app.Application

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/19
 * @desc   :
 */
interface BaseAppInit {

    /**需要在第一时间被初始化的*/
    fun onInitSpeed(app:Application)

    /**低优先级初始化*/
    fun onInitLow(app:Application)
}