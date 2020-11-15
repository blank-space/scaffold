package com.bsnl.common.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import android.util.Log
import com.bsnl.base.BuildConfig
import com.bsnl.base.log.L
import timber.log.Timber

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   : 优化工具
 */

/**
 * @see MessageQueue.IdleHandler.queueIdle
 */
@JvmOverloads
fun doOnMainThreadIdle(action: () -> Unit, timeout: Long? = null) {
    val handler = Handler(Looper.getMainLooper())
    val idleHandler = MessageQueue.IdleHandler {
        handler.removeCallbacksAndMessages(null)
        action()
        return@IdleHandler false
    }

    fun setupIdleHandler(queue: MessageQueue) {
        if (timeout != null) {
            handler.postDelayed({
                queue.removeIdleHandler(idleHandler)
                action()
                if(BuildConfig.LOG_DEBUG) {
                    Log.e("TAG","${timeout}ms timeOut! ")
                }

            }, timeout)
        }
        queue.addIdleHandler(idleHandler)
        handler.sendEmptyMessage(0)
    }

    if (Looper.getMainLooper() == Looper.myLooper()) {
        setupIdleHandler(Looper.myQueue())
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setupIdleHandler(Looper.getMainLooper().queue)
        } else {
            //6.0以下不能直接调用Looper.queue
            handler.post { setupIdleHandler(Looper.myQueue()) }
        }
    }

}