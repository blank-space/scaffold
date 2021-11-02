package com.dawn.launch.app

import android.content.Context
import android.view.View
import com.dawn.launch.app.R
import com.kingja.loadsir.callback.Callback

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   :
 */
class LoadingLayout : Callback() {

    override fun onCreateView() = R.layout.app_activity_loading

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        /** 拦截reload，只让ErrorLayoutCallback相应event */
        return true
    }
}