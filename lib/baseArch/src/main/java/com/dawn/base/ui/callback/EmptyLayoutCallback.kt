package com.dawn.base.ui.callback

import android.content.Context
import android.view.View
import com.dawn.base.R
import com.dawn.base.utils.showToast
import com.kingja.loadsir.callback.Callback

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   :
 */
class EmptyLayoutCallback : Callback() {
    override fun onCreateView()=  R.layout.base_empty_layout

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        "去寻找花花世界吧".showToast()
        /** 拦截reload，只让ErrorLayoutCallback相应event */
        return true
    }

}