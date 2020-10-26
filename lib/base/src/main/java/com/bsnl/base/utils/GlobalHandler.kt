package com.bsnl.base.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   : 全局的Handler
 */
object GlobalHandler {
    private var sHandler: Handler? = null

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

    fun postDelayed(r: Runnable?, delay: Long) {
        prepare()
        sHandler!!.postAtTime(r, SystemClock.uptimeMillis() + delay)
    }

    fun postDelayed(r: Runnable?, token: Any?, delay: Long) {
        prepare()
        val message = Message.obtain(sHandler, r)
        message.obj = token
        sHandler!!.postAtTime(r, token, SystemClock.uptimeMillis() + delay)
    }

    fun remove(token: Any?) {
        prepare()
        sHandler!!.removeCallbacksAndMessages(token)
    }

    @Synchronized
    private fun prepare() {
        if (sHandler == null) {
            sHandler = Handler(Looper.getMainLooper())
        }
    }
}
