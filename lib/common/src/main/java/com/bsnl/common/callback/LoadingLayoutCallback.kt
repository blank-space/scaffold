package com.bsnl.common.callback

import android.content.Context
import android.view.View
import com.bsnl.base.log.L
import com.bsnl.common.R
import com.kingja.loadsir.callback.Callback

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/7
 * @desc   :
 */
class LoadingLayoutCallback : Callback() {

    override fun onCreateView() = R.layout.common_stub_progress_wait

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        /** 拦截reload，只让ErrorLayoutCallback相应event */
        return true
    }
}