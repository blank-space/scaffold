package com.bsnl.launch.app

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bsnl.base.ActivityLifecycleCallback
import com.bsnl.base.BaseApp
import com.bsnl.base.BaseAppInit
import com.bsnl.base.log.L
import com.bsnl.base.utils.GlobalHandler
import com.bsnl.common.callback.EmptyLayoutCallback
import com.bsnl.common.callback.ErrorLayoutCallback
import com.bsnl.common.callback.LoadingLayoutCallback
import com.bsnl.faster.TaskDispatcher
import com.bsnl.launch.app.task.InitLogTask
import com.bsnl.launch.app.task.InitMVVMTask
import com.bsnl.launch.app.task.InitWebViewTask
import com.bsnl.sample.pkg.feature.hook.BitmapsHook
import com.bsnl.sample.pkg.feature.hook.DrawableHook
import com.kingja.loadsir.core.LoadSir
import de.robv.android.xposed.DexposedBridge


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */

class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        initTasks()
        initSubModulesSpeed()
        registerActivityLifecycleCallbacks(ActivityLifecycleCallback())
        initHookMethods()
        initLoadSir()
        initSubModulesLow()
        loop()
    }

    private fun initHookMethods() {
        //不用于线上，避免出现兼容问题
        if (BuildConfig.DEBUG) {
            DexposedBridge.findAndHookMethod(
                ImageView::class.java,
                "setImageBitmap",
                Bitmap::class.java,
                BitmapsHook()
            )
            DexposedBridge.findAndHookMethod(
                ImageView::class.java,
                "setImageDrawable",
                Drawable::class.java,
                DrawableHook()
            )
        }
    }

    private fun initLoadSir() {
        LoadSir.beginBuilder()
            .addCallback(ErrorLayoutCallback())
            .addCallback(EmptyLayoutCallback())
            .addCallback(LoadingLayoutCallback())
            .setDefaultCallback(LoadingLayoutCallback::class.java)
            .commit()
    }

    private fun initTasks() {
        TaskDispatcher.init(this)
        val dispatcher: TaskDispatcher = TaskDispatcher.createInstance()
        dispatcher.addTask(InitLogTask())
            .addTask(InitMVVMTask())
            .addTask(InitWebViewTask())
            .start()
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

    private fun initSubModulesSpeed() {
        PageConfig.initModules.forEach {
            try {
                val clazz = Class.forName(it)
                val moduleInit = clazz.newInstance() as BaseAppInit
                moduleInit.onInitSpeed(this)
            } catch (e: Exception) {
                e.message?.let { it1 -> L.e(it1) }
            }
        }
    }

    private fun initSubModulesLow() {
        PageConfig.initModules.forEach {
            try {
                val clazz = Class.forName(it)
                val moduleInit = clazz.newInstance() as BaseAppInit
                moduleInit.onInitLow(this)
            } catch (e: Exception) {
                e.message?.let { it1 -> L.e(it1) }
            }
        }
    }
}