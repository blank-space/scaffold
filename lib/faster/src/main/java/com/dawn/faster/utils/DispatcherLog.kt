package com.dawn.faster.utils

import android.util.Log

object DispatcherLog {
    var isDebug = true
    fun i(msg: String?) {
        if (!isDebug) {
            return
        }
        if (msg != null) {
            Log.i("TaskDispatcher", msg)
        }
    }

}