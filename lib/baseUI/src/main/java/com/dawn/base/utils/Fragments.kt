package com.dawn.base.utils

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/1
 * @desc   :
 */

fun Fragment.withArguments(vararg pairs: Pair<String, *>) = apply {
    arguments = bundleOf(*pairs)
}

inline fun <reified T> Fragment.arguments(key: String) = lazy<T?> {
    arguments?.get(key) as T
}

inline fun <reified T> Fragment.arguments(key: String, default: T) = lazy {
    arguments?.get(key)?: default
}

inline fun <reified T> Fragment.safeArguments(name: String) = lazy<T> {
    checkNotNull(arguments?.get(name) as T) { "No intent value for key \"$name\"" }
}

/**
 * 共享Activity
 */
inline fun <reified T : ViewModel> Fragment.getVm(): T {
    return ViewModelProvider(this.activity as FragmentActivity).get(T::class.java)
}

/**
 * 独立作用域
 */
inline fun <reified T : ViewModel> Fragment.getSelfVm(): T {
    return ViewModelProvider(this).get(T::class.java)
}
