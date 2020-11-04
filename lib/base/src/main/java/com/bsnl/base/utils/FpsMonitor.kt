package com.bsnl.base.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Choreographer

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/4
 * @desc   : Fps检测
 */
object FpsMonitor {

    private const val FPS_INTERVAL_TIME = 1000L
    private var count = 0
    private var isFpsOpen = false
    private val fpsRunnable by lazy { FpsRunnable() }
    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    private val listeners = arrayListOf<(Int) -> Unit>()

    fun startMonitor(listener: (Int) -> Unit) {
        // 防止重复开启
        if (!isFpsOpen) {
            isFpsOpen = true
            listeners.add(listener)
            mainHandler.postDelayed(fpsRunnable, FPS_INTERVAL_TIME)
            Choreographer.getInstance().postFrameCallback(fpsRunnable)
        }
    }

    fun stopMonitor() {
        count = 0
        mainHandler.removeCallbacks(fpsRunnable)
        Choreographer.getInstance().removeFrameCallback(fpsRunnable)
        isFpsOpen = false
    }

    fun isOpen() = isFpsOpen

    class FpsRunnable : Choreographer.FrameCallback, Runnable {
        override fun doFrame(frameTimeNanos: Long) {
            //每帧执行一次，记录count,总计一秒内执行了多少帧
            count++
            Choreographer.getInstance().postFrameCallback(this)
        }

        override fun run() {
            //把count回调出去
            listeners.forEach { it.invoke(count) }
            //清零
            count = 0
            mainHandler.postDelayed(this, FPS_INTERVAL_TIME)
        }
    }
}