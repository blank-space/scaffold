
package com.bsnl.launch.app

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bsnl.base.ActivityLifecycleCallback
import com.bsnl.base.BaseApp
import com.bsnl.base.log.L
import com.bsnl.base.utils.GlobalHandler
import com.bsnl.faster.TaskDispatcher
import com.bsnl.launch.app.task.InitImageLoaderTask
import com.bsnl.launch.app.task.InitLogTask
import com.bsnl.launch.app.task.InitMVVMTask
import com.bsnl.launch.app.task.InitWebViewTask
import com.bsnl.sample.pkg.feature.hook.BitmapsHook
import com.bsnl.sample.pkg.feature.hook.DrawableHook
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
        registerActivityLifecycleCallbacks(ActivityLifecycleCallback())

        DexposedBridge.findAndHookMethod(ImageView::class.java, "setImageBitmap", Bitmap::class.java, BitmapsHook())
        DexposedBridge.findAndHookMethod(ImageView::class.java, "setImageDrawable", Drawable::class.java, DrawableHook())

        loop()
    }


    private fun initTasks() {
        TaskDispatcher.init(this)
        val dispatcher: TaskDispatcher = TaskDispatcher.createInstance()
        dispatcher.addTask(InitLogTask())
            .addTask(InitMVVMTask())
            .addTask(InitImageLoaderTask())
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
}