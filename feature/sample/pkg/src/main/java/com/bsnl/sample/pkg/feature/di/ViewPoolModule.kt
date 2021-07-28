package com.bsnl.sample.pkg.feature.di

import com.bsnl.base.BaseApp
import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.ViewHelper

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