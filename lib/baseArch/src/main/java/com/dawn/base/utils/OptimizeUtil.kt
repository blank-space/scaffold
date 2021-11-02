package com.dawn.base.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import com.dawn.base.BuildConfig
import com.dawn.base.log.L

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
                if (BuildConfig.LOG_DEBUG) {
                    L.e( "after${timeout}ms invoke action! ")
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


/**
 * 实现将子线程Looper中的MessageQueue替换为主线程中Looper的
 * MessageQueue，这样就能够在子线程中异步创建UI。
 *
 * 注意：需要在子线程中调用。
 *
 * @param reset 是否将子线程中的MessageQueue重置为原来的，false则表示需要进行替换
 * @return 替换是否成功
 */
fun replaceLooperWithMainThreadQueue(reset: Boolean): Boolean {
    return if (Looper.getMainLooper() == Looper.myLooper()) {
        true
    } else {
        // 1、获取子线程的ThreadLocal实例
        val threadLocal: ThreadLocal<Looper> =
            ReflectUtils.reflect(Looper::class.java).field("sThreadLocal").get()
        run {
            var looper: Looper? = null
            if (!reset) {
                Looper.prepare()
                looper = Looper.myLooper()
                // 2、通过调用MainLooper的getQueue方法区获取主线程Looper中的MessageQueue实例
                val queue = ReflectUtils.reflect(Looper.getMainLooper()).method("getQueue").get() as? MessageQueue ?: return false
                // 3、将子线程中的MessageQueue字段的值设置为主线的MessageQueue实例
                ReflectUtils.reflect(looper).field("mQueue", queue)
            }

            // 4、reset为false，表示需要将子线程Looper中的MessageQueue重置为原来的。
            ReflectUtils.reflect(threadLocal).method("set", looper)
            true
        }
    }
}