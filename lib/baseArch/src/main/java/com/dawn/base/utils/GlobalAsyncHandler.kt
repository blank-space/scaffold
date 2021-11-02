package com.dawn.base.utils

import android.os.*
import androidx.core.os.HandlerCompat
import androidx.core.os.MessageCompat

/**
 * @author : LeeZhaoXing
 * @date   : 2021/1/28
 * @desc   : 全局的异步Handler
 *
 *  Handler#post()默认情况下，这会遵循VSYNC锁定，并且将导致等待直到下一帧开始运行。
 *  每次发射经过时，最多延迟16ms执行。更糟的是，即使您已经在主线程上，也会发生这种情况。
 *  AsyncHandler突破VSYNC同步消息屏障，如果主线程调度程序已经在主线程上，它可以立即运行工作并避免VSYNC开销。
 *  使用[MessageCompat.setAsynchronous]或者[HandlerCompat.createAsync]创建的async Message被post到主线程队列中，他们依然是按顺序执行的
 *  <p>
 *      1.如果post的内容没有与其他内容有严格的顺序要求，最好使用Async Handler
 *      2.几乎所有不属于自定义View的代码都不需要VSYNC屏障来保证正确性，如有需要请用普通的Handler
 *  </p>
 */
object GlobalAsyncHandler {
    private var sHandler: Handler? = null

    fun getInstanceWithCallback(callback:Handler.Callback): Handler {
        if (sHandler == null) {
            sHandler = HandlerCompat.createAsync(Looper.getMainLooper(),callback)
        }
        return sHandler!!
    }

    fun post(r: Runnable) {
        prepare()
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run()
        } else {
            sHandler!!.post(r)
        }
    }


    fun post(r: Runnable, token: Any?) {
        prepare()
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run()
        } else {
            sHandler!!.postAtTime(r, token, SystemClock.uptimeMillis())
        }
    }

    fun postDelayed( delay: Long,r: Runnable?) {
        prepare()
        if (r != null) {
            sHandler!!.postAtTime(r, SystemClock.uptimeMillis() + delay)
        }
    }

    fun postDelayed(r: Runnable?, token: Any?, delay: Long) {
        prepare()
        val message = Message.obtain(sHandler, r)
        message.obj = token
        if (r != null) {
            sHandler!!.postAtTime(r, token, SystemClock.uptimeMillis() + delay)
        }
    }

    fun remove(token: Any?) {
        prepare()
        sHandler!!.removeCallbacksAndMessages(token)
    }

    @Synchronized
    private fun prepare() {
        if (sHandler == null) {
            sHandler = HandlerCompat.createAsync(Looper.getMainLooper())
        }
    }



}
