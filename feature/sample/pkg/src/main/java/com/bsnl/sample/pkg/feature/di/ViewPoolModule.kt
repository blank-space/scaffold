package com.bsnl.sample.pkg.feature.di

import com.bsnl.base.BaseApp
import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.ViewHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/30
 * @desc   :
 */
@Module
@InstallIn(ApplicationComponent::class)
object ViewPoolModule {
    @Singleton
    @Provides
    fun provideViewHelper(): ViewHelper {
        return ViewHelper.getInstance(BaseApp.application)
    }
}