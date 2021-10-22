package com.dawn.base

import android.app.Application
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import coil.util.DebugLogger
import com.alibaba.android.arouter.launcher.ARouter
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/10
 * @desc   :
 */
open class BaseApp : Application(), ViewModelStoreOwner, ImageLoaderFactory {

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

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .componentRegistry {
                // GIFs
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(this@BaseApp))
                } else {
                    add(GifDecoder())
                }
                // SVGs
                add(SvgDecoder(this@BaseApp))
                // Video frames
                //add(VideoFrameDecoder.Factory())
            }
            .availableMemoryPercentage(0.25)
            .okHttpClient {
                val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .dispatcher(dispatcher)
                    .build()
            }
            .crossfade(true)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger(Log.VERBOSE))
                }
            }
            .build()
    }
}


