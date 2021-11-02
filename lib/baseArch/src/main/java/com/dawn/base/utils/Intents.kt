package com.dawn.base.utils

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/1
 * @desc   :
 */
inline fun <reified T> Activity.intentExtras(name: String) = lazy<T?> {
    intent.extras?.get(name) as T
}


inline fun <reified T> Activity.intentExtras(name: String, default: T) = lazy {
    intent.extras?.get(name) ?: default
}


inline fun <reified T> Activity.safeIntentExtras(name: String) = lazy<T> {
    checkNotNull(intent.extras?.get(name) as T) { "No intent value for key \"$name\"" }
}




