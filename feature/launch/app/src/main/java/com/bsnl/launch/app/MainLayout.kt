package com.bsnl.launch.app

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import  android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import com.bsnl.base.dsl.dp
import com.bsnl.base.dsl.onClick
import com.bsnl.base.utils.ApiUtils
import com.bsnl.base.widget.CustomLayout
import com.bsnl.constraint.export.api.ConstrainApi
import com.bsnl.sample.export.api.SampleApi

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/21
 * @desc   : Custom ViewGroup > WIDTH:MATCH_PARENT , HEIGHT:WRAP_CONTENT
 */
@SuppressLint("ResourceAsColor")
class MainLayout(context: Context) : CustomLayout(context) {

    val textStyleId = R.style.feature_launch_app_text

    val header = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.FIT_XY
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 180.dp)
        setImageResource(R.mipmap.feature_launch_app_ic_head)
        addView(this)
    }

    val avatar = AppCompatImageView(context).apply {
        setImageResource(R.drawable.feature_launch_app_ic_logo)
        layoutParams = LayoutParams(80.dp, 80.dp)
        addView(this)
    }

    val listItem = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "ListView"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        onClick = {
            ApiUtils.getApi(SampleApi::class.java).startSampleActivity(context)
        }
        addView(this)
    }

    val dataBinding = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "CustomView with DataBinding"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            ApiUtils.getApi(SampleApi::class.java).startDataBindingActivity(context)
        }
        addView(this)
    }

    val viewPager = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "ViewPager"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            ApiUtils.getApi(SampleApi::class.java).startViewPagerActivity(context)
        }
        addView(this)
    }

    val constraintLayout = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "ConstraintLayout"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            ApiUtils.getApi(ConstrainApi::class.java).startSampleActivity(context)
        }
        addView(this)
    }

    val webView = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "WebView"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            ApiUtils.getApi(SampleApi::class.java).startWebViewActivity(context, "https://wanandroid.com/?cid=440")
        }
        addView(this)
    }

    val asyncCreateView = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "AsyncCreateView"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            ApiUtils.getApi(SampleApi::class.java)
                .startAsyncCreateViewActivity(context)
        }
        addView(this)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        header.autoMeasure()
        avatar.autoMeasure()
        listItem.autoMeasure()
        dataBinding.autoMeasure()
        viewPager.autoMeasure()
        constraintLayout.autoMeasure()
        webView.autoMeasure()
        asyncCreateView.autoMeasure()
        val wrapContentHeight = (header.measuredHeight + listItem.measuredHeightWithMargins+dataBinding.measuredHeightWithMargins*5)
        setMeasuredDimension(measuredWidth, wrapContentHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        header.layout(0, 0)
        avatar.let {
            it.layout(
                (measuredWidth / 2 - it.measuredWidth / 2),
                (header.measuredHeight / 2 - it.measuredHeight / 2)
            )
        }
        listItem.let { it.layout(header.left, header.bottom + it.marginTop) }
        dataBinding.let { it.layout(listItem.left, listItem.bottom + it.marginTop) }
        viewPager.let { it.layout(dataBinding.left, dataBinding.bottom + it.marginTop) }
        constraintLayout.let { it.layout(viewPager.left, viewPager.bottom + it.marginTop) }
        webView.let { it.layout(constraintLayout.left, constraintLayout.bottom + it.marginTop) }
        asyncCreateView.let { it.layout(webView.left, webView.bottom + it.marginTop) }
    }
}


