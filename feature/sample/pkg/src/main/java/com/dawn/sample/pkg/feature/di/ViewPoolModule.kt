package com.dawn.sample.pkg.feature.di

import com.dawn.base.BaseApp
import com.dawn.sample.pkg.feature.view.async.asyncLoadView.ViewHelper

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/30
 * @desc   :
 */

object ViewPoolModule {

    fun provideViewHelper(): ViewHelper {
        val obj =ViewHelper.getInstance(BaseApp.application)
        obj.refreshCurrentActivity(BaseApp.application)
        return obj
    }
}