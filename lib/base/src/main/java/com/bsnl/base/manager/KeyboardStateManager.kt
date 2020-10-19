package com.bsnl.base.manager

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bsnl.base.utils.KeyboardUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/14
 * @desc   : 自动关闭键盘
 */
object KeyboardStateManager : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        if(owner is Fragment){
            owner.activity?.let { KeyboardUtils.hideSoftInput(it) }
        }else if(owner is Activity) {
            KeyboardUtils.hideSoftInput(owner)
        }
        super.onStop(owner)
    }


}