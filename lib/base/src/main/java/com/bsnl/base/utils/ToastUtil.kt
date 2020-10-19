package com.bsnl.base.utils

import android.os.Looper
import android.widget.Toast
import com.bsnl.base.BaseApp


private var toast: Toast? = null


/**
 * @sample "用户名或密码不完整".showToast()
 */
fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        if (toast == null) {
            toast = Toast.makeText(BaseApp.application, this, duration)
        } else {
            toast?.setText(this)
        }
        toast?.show()
    }
}


/**
 * @sample getString(R.string.name).showToast()
 */
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        if (toast == null) {
            toast = Toast.makeText(BaseApp.application, this, duration)
        } else {
            toast?.setText(this)
        }
        toast?.show()
    }
}
