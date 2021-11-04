package com.dawn.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.ui.callback.EmptyLayoutCallback
import com.dawn.base.ui.callback.ErrorLayoutCallback
import com.dawn.base.ui.page.iface.ViewState
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.ProgressCallback
import com.kingja.loadsir.core.LoadSir

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
    private val loadModuleProxy by lazy { LoadModuleProxy() }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        mAppViewModelStore = ViewModelStore()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
        loadModuleProxy.onInitSpeed(application)
        loadModuleProxy.onInitLow(application)
        initLoadSir()
    }

    open fun initLoadSir() {
        val loadingCallback: ProgressCallback = ProgressCallback.Builder()
            .setTitle("Loading", R.style.Hint_Title).build()

        LoadSir.beginBuilder()
            .addCallback(ErrorLayoutCallback())
            .addCallback(EmptyLayoutCallback())
            .addCallback(loadingCallback)
            .commit()
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }


}


