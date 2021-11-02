package com.dawn.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawn.base.BaseApp

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/1
 * @desc   :
 */

/**
 * 传参启动Activity
 */
inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}


/**
 * 启动Activity
 */
inline fun <reified T> startActivity(context: Context) {
    val intent = Intent(context, T::class.java)
    context.startActivity(intent)
}

inline fun <reified T : ViewModel> FragmentActivity.getVm(): T {
    return ViewModelProvider(this).get(T::class.java)
}


inline fun <reified T : ViewModel> Activity.getApplicationScopeViewModel(): T {
    return ViewModelProvider(application as BaseApp).get(T::class.java)
}

fun Activity.finishWithResult(vararg pairs: Pair<String, *>) {
    setResult(Activity.RESULT_OK, Intent().apply { putExtras(bundleOf(*pairs)) })
    finish()
}