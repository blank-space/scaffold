package com.dawn.launch.app.task

import android.os.Process;
import android.util.Log;

import androidx.core.os.TraceCompat;
import com.caij.app.startup.Task
import com.caij.app.startup.TaskListener
import com.dawn.launch.app.ThreadManager


/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/25
 * @desc   :
 */
class MonitorTaskListener(tag: String, isLog: Boolean) : TaskListener {
    private val tag: String
    private val isLog: Boolean
    override fun onWaitRunning(task: Task?) {}
    override fun onStart(task: Task) {
        if (isLog) {
            Log.d("$tag-START", "task start :" + task.getTaskName())
        }
        TraceCompat.beginSection(task.getTaskName())
        if (task.isWaitOnMainThread()) {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        }
    }

    override fun onFinish(task: Task, dw: Long, df: Long) {
        if (task.isWaitOnMainThread()) {
            android.os.Process.setThreadPriority(ThreadManager.DEFAULT_PRIORITY)
        }
        TraceCompat.endSection()
        if (isLog) {
            Log.d("$tag-END",
                "task end :" + task.getTaskName()
                    .toString() + " wait " + dw.toString() + " cost " + df
            )
        }
    }

    init {
        this.tag = tag
        this.isLog = isLog
    }
}