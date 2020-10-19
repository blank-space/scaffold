package com.bsnl.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.bsnl.base.imageloader.BaseImageLoaderStrategy
import com.bsnl.base.imageloader.ImageLoader
import com.bsnl.base.imageloader.glide.GlideImageLoaderStrategy
import com.bsnl.base.log.L

import com.bsnl.base.utils.ActivitysManager.addActivity
import com.bsnl.base.utils.ActivitysManager.removeActivity
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

    override fun onCreate() {
        super.onCreate()
        application = this
        //本地Key-value存储
        MMKV.initialize(this)
        //日志框架
        L.init()


        mAppViewModelStore = ViewModelStore()

        registerActivityLifecycleCallbacks()
        //配置Glide加载策略
        ImageLoader.loadImgStrategy = GlideImageLoaderStrategy() as BaseImageLoaderStrategy<Any>


    }


    private fun registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Timber.v("${activity::class.java.name}#onActivityCreated ")
                addActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityStarted(activity: Activity) {
                L.v("${activity::class.java.name}#onActivityStarted ")
            }


            override fun onActivityResumed(activity: Activity) {
                L.v("${activity::class.java.name}#onActivityResumed ")

            }

            override fun onActivityPaused(activity: Activity) {
                L.v("${activity::class.java.name}#onActivityPaused ")
            }

            override fun onActivityStopped(activity: Activity) {
                L.v("${activity::class.java.name}#onActivityStopped ")
            }

            override fun onActivityDestroyed(activity: Activity) {
                L.v("${activity::class.java.name}#onActivityDestroyed ")
                fixSoftInputLeaks(activity)
                removeActivity(activity)
            }
        })
    }

    private fun fixSoftInputLeaks(activity: Activity?) {
        if (activity == null) {
            return
        }
        val imm = application.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
        for (leakView in leakViews) {
            try {
                val leakViewField =
                    InputMethodManager::class.java.getDeclaredField(leakView) ?: continue

                if (!leakViewField.isAccessible) {
                    leakViewField.isAccessible = true
                }
                val obj = leakViewField[imm] as? View ?: continue
                if (obj.rootView === activity.window.decorView.rootView) {
                    leakViewField[imm] = null
                }
            } catch (ignore: Throwable) {
                /**/
            }
        }
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }


}