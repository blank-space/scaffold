package com.bsnl.faster.utils

import android.util.Log

object DispatcherLog {
    var isDebug = true
    fun i(msg: String?) {
        if (!isDebug) {
            return
        }
        Log.i("TaskDispatcher", msg)
    }

}