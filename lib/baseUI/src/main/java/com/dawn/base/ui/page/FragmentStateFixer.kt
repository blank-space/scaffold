package com.dawn.base.ui.page

import android.content.Context
import android.os.Build
import android.os.Bundle

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/22
 * @desc   :
 */
object FragmentStateFixer {

    private const val SAVED_COMPONENTS_KEY = "androidx.lifecycle.BundlableSavedStateRegistry.key"

    fun fixState(context: Context, bundle: Bundle?) {
        if (bundle != null && checkCondition()) {
            bundle.classLoader = context.javaClass.classLoader
            /** 需要修改的是bundle下androidx.lifecycle.BundlableSavedStateRegistry.key中子项的classloader*/
            bundle.getBundle(SAVED_COMPONENTS_KEY)?.let {
                it.keySet()?.forEach { key ->
                    (it.get(key) as? Bundle)?.classLoader = context.javaClass.classLoader
                }
            }
        }
    }

    private fun checkCondition(): Boolean {
        return !(Build.VERSION.SDK_INT != 29 && Build.VERSION.SDK_INT != 28)
    }
}