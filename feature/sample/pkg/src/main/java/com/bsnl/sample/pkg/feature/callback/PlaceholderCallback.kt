package com.bsnl.sample.pkg.feature.callback

import android.content.Context
import android.view.View
import com.bsnl.sample.pkg.R
import com.kingja.loadsir.callback.Callback

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   :
 */
class PlaceholderCallback : Callback() {

    override fun onCreateView() =  R.layout.feature_sample_pkg_activity_layout_placeholder

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        /** 拦截reload，只让ErrorLayoutCallback响应event */
        return true
    }
}