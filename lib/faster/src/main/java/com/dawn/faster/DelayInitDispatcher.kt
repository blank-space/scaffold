package com.dawn.faster

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import java.util.*

/**
 * 延迟初始化分发器
 */
class DelayInitDispatcher {
    private val mDelayTasks: Queue<Task> = LinkedList()
    private val mIdleHandler = IdleHandler {
        if (mDelayTasks.size > 0) {
            val task = mDelayTasks.poll()
            DispatchRunnable(task).run()
        }
        !mDelayTasks.isEmpty()
    }

    fun addTask(task: Task): DelayInitDispatcher {
        mDelayTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(mIdleHandler)
    }
}