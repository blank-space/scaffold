package com.bsnl.common.callback

import android.content.Context
import android.view.View
import com.bsnl.base.utils.showToast
import com.bsnl.common.R
import com.kingja.loadsir.callback.Callback

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   :
 */
class EmptyLayoutCallback : Callback() {
    override fun onCreateView()=  R.layout.common_empty_layout

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        "去寻找花花世界吧".showToast()
        /** 拦截reload，只让ErrorLayoutCallback相应event */
        return true
    }

}