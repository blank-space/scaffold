package com.bsnl.launch.app

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.base.dsl.onClick
import com.bsnl.base.widget.CustomLayout
import com.bsnl.sample.export.api.ISampleService
import com.bsnl.sample.export.path.SamplePath
import com.bsnl.sample.pkg.feature.view.countdowm.CountDownActivity
import com.bsnl.sample.pkg.feature.view.gson.GsonDemo
import com.bsnl.sample.pkg.feature.view.gson.GsonDemoActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/21
 * @desc   :
 */
@SuppressLint("ResourceAsColor")
class MainLayout(context: Context, sampleService: ISampleService?) : CustomLayout(context) {
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
            //vehicleService?.startListViewActivity()
            ARouter.getInstance().build(SamplePath.A_LISTVIEW_ACTIVITY).navigation()
        }
        addView(this)
    }

    val webView = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "WebView"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            sampleService?.startWebViewActivity("https://www.wanandroid.com")
        }
        addView(this)
    }

    val gson = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "Gson解析"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            GsonDemoActivity.actionStart(context)
        }
        addView(this)
    }

    val countdown = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "倒计时列表"
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40.dp)
        (layoutParams as LayoutParams).topMargin = 5.dp
        onClick = {
            CountDownActivity.actionStart(context)
        }
        addView(this)
    }


    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEachAutoMeasure()
        setMeasuredDimension(measuredWidth, measuredHeight)
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
        webView.let { it.layout(listItem.left, listItem.bottom + it.marginTop) }
        gson.let { it.layout(listItem.left, webView.bottom + it.marginTop) }
        countdown.let { it.layout(listItem.left, gson.bottom + it.marginTop) }
    }
}


