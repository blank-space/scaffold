package com.bsnl.base

import android.R
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.base.imageloader.BaseImageLoaderStrategy
import com.bsnl.base.imageloader.ImageLoader
import com.bsnl.base.imageloader.glide.GlideImageLoaderStrategy
import com.bsnl.base.log.L

import com.bsnl.base.utils.ActivitysManager.addActivity
import com.bsnl.base.utils.ActivitysManager.removeActivity
import com.bsnl.base.widget.ShowFps
import com.tencent.mmkv.MMKV
import timber.log.Timber

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/10
 * @desc   :
 */
open class BaseApp : Application(), ViewModelStoreOwner {

    companion object {
        lateinit var application: Application
    }

    private lateinit var mAppViewModelStore: ViewModelStore


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        mAppViewModelStore = ViewModelStore()
        if (BuildConfig.DEBUG) {
            // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
    }




    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }





}


