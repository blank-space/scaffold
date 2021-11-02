package com.dawn.base.utils

import android.os.Looper
import android.widget.Toast
import com.dawn.base.BaseApp




/**
 * @sample "用户名或密码不完整".showToast()
 */
fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(BaseApp.application, this, duration).show()
    }
}


/**
 * @sample getString(R.string.name).showToast()
 */
fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(BaseApp.application, this, duration).show()
    }
}
