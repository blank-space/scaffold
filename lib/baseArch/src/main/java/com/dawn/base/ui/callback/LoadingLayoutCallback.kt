package com.dawn.base.ui.callback

import android.content.Context
import android.view.View
import com.dawn.base.R
import com.kingja.loadsir.callback.Callback

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   :
 */
class LoadingLayoutCallback : Callback() {

    override fun onCreateView() = R.layout.base_loading_layout

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        /** 拦截reload，只让ErrorLayoutCallback相应event */
        return true
    }
}