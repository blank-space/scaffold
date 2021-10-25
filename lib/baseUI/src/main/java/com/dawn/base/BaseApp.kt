package com.dawn.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter

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


