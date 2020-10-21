package com.bsnl.common.ui.viewStatus.adapter

import android.view.View
import com.bsnl.common.constant.HIDE_LOADING_STATUS_MSG
import com.bsnl.common.ui.viewStatus.adapter.view.GlobalLoadingStatusView
import com.bsnl.common.ui.viewStatus.Gloading

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/20
 * @desc   : 全局页面状态管理适配器
 */
class GlobalAdapter : Gloading.Adapter {
    override fun getView(holder: Gloading.Holder?, convertView: View?, status: Int): View? {
        var loadingStatusView: GlobalLoadingStatusView? = null
        //reuse the old view, if possible
        if (convertView != null && convertView is GlobalLoadingStatusView) {
            loadingStatusView = convertView
        }
        if (loadingStatusView == null) {
            loadingStatusView = GlobalLoadingStatusView(holder?.context, holder?.retryTask)
        }
        loadingStatusView?.setStatus(status)
        holder?.getData<Any>().let {
            val data =it
            val hideMsgView: Boolean = HIDE_LOADING_STATUS_MSG.equals(data)
            loadingStatusView?.setMsgViewVisibility(!hideMsgView)
        }
        //show or not show msg view

        return loadingStatusView
    }

}