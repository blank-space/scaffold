package com.dawn.launch.app

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import coil.util.DebugLogger
import com.caij.app.startup.Config
import com.caij.app.startup.DGAppStartup
import com.caij.app.startup.OnProjectListener
import com.dawn.base.ActivityLifecycleCallback
import com.dawn.base.BaseApp
import com.dawn.base.log.L
import com.dawn.base.utils.GlobalHandler
import com.dawn.launch.app.task.InitLogTask
import com.dawn.launch.app.task.InitMVVMTask
import com.dawn.launch.app.task.MonitorTaskListener
import com.dawn.sample.pkg.feature.hook.BitmapsHook
import com.dawn.sample.pkg.feature.hook.DrawableHook
import dagger.hilt.android.HiltAndroidApp
import de.robv.android.xposed.DexposedBridge
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
@HiltAndroidApp
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        initTasks()
        registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
        loop()
    }

    private fun initTasks() {
        val config = Config()
        config.isStrictMode = BuildConfig.DEBUG
        DGAppStartup.Builder()
            .add(InitLogTask())
            .add(InitMVVMTask())
            .setConfig(config)
            .addTaskListener(MonitorTaskListener("@@", true))
            .setExecutorService(ThreadManager.getInstance().WORK_EXECUTOR)
            .addOnProjectExecuteListener(object : OnProjectListener {
                override fun onProjectStart() {}
                override fun onProjectFinish() {}
                override fun onStageFinish() {}
            })
            .create()
            .start()
            .await()
    }


    /**
     * 接管Loop,处理一些系统级的没必要的闪退：当他们发生时进行try-catch并不会引起上下文混乱
     */
    fun loop() {
        GlobalHandler.post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    val stack = Log.getStackTraceString(e)
                    if (stack.contains("Toast")) {
                        L.e("捕获到异常，在线程${Thread.currentThread().name}, 在${e.stackTrace[0].className}")
                    } else {
                        //不可接受的异常，让他crash
                        throw  e
                    }
                }
            }
        }
    }




}