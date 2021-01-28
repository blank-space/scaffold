package com.bsnl.common.ui.viewStatus.adapter.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bsnl.base.BaseApp

import com.bsnl.common.R
import com.bsnl.common.ui.viewStatus.Gloading
import com.bsnl.common.utils.NetworkUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/20
 * @desc   : 全局页面状态设置示例
 */
@SuppressLint("ViewConstructor")
class GlobalLoadingStatusView(
    context: Context?,
    retryTask: Runnable?
) : LinearLayout(context), View.OnClickListener {
    private val mTextView: TextView
    private val mRetryTask: Runnable?
    private val mImageView: ImageView
    fun setMsgViewVisibility(visible: Boolean) {
        mTextView.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setStatus(status: Int) {
        var show = true
        var onClickListener: OnClickListener? = null
        var image: Int = R.drawable.lib_common_loading
        var str: Int = R.string.lib_common_str_none
        when (status) {
            Gloading.STATUS_LOAD_SUCCESS -> show = false
            Gloading.STATUS_LOADING -> str = R.string.lib_common_loading
            Gloading.STATUS_LOAD_FAILED -> {
                str = R.string.lib_common_load_failed
                image = R.drawable.lib_common_icon_failed
                val networkConn: Boolean = NetworkUtils.isConnected(BaseApp.application)
                if (networkConn != null && !networkConn) {
                    str = R.string.lib_common_load_failed_no_network
                    image = R.drawable.lib_common_icon_no_wifi
                }
                onClickListener = this
            }
            Gloading.STATUS_EMPTY_DATA -> {
                str = R.string.lib_common_empty
                image = R.drawable.lib_common_icon_empty
            }
            else -> {
            }
        }
        mImageView.setImageResource(image)
        setOnClickListener(onClickListener)
        mTextView.setText(str)
        visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        mRetryTask?.run()
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.lib_common_view_global_loading_status, this, true)
        mImageView = findViewById(R.id.image)
        mTextView = findViewById(R.id.text)
        mRetryTask = retryTask
        setBackgroundColor(-0xf0f10)
    }
}