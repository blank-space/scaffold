package com.dawn.sample.pkg.feature.view.async.asyncLoadView

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/29
 * @desc   :全局缓存，View的context 问题解决方案
 */
class ViewContext(realContext: Context) : ContextWrapper(realContext) {
    private var mCrtContextRef = WeakReference(realContext)

    fun setCurrentContext(ctx: Context) {
        mCrtContextRef = WeakReference(ctx)
    }

    fun getCurrentContext() = mCrtContextRef.get()

    fun getActivity(context: Context): Activity? {
        if (context is ViewContext) {
            val ctx = getCurrentContext()
            return ctx as Activity
        } else if (context is Activity) return context
        return null
    }

}