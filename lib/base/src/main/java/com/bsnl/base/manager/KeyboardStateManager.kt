package com.bsnl.base.manager

import android.app.Activity
import android.app.Application
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bsnl.base.BaseApp
import com.bsnl.base.log.L
import com.bsnl.base.utils.KeyboardUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/14
 * @desc   : 自动关闭键盘
 */
object KeyboardStateManager : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        if (owner is Fragment) {
            owner.activity?.let { KeyboardUtils.hideSoftInput(it)
                fixSoftInputLeaks(it)}
        } else if (owner is Activity) {
            KeyboardUtils.hideSoftInput(owner)
            fixSoftInputLeaks(owner)
        }

        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    private fun fixSoftInputLeaks(activity: Activity?) {
        if (activity == null) {
            return
        }
        val imm =
            BaseApp.application.getSystemService(Application.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
        for (leakView in leakViews) {
            try {
                val leakViewField =
                    InputMethodManager::class.java.getDeclaredField(leakView) ?: continue

                if (!leakViewField.isAccessible) {
                    leakViewField.isAccessible = true
                }
                val obj = leakViewField[imm] as? View ?: continue
                if (obj.rootView === activity.window.decorView.rootView) {
                    leakViewField[imm] = null
                }
            } catch (ignore: Throwable) {
                /**/
            }
        }
    }


}