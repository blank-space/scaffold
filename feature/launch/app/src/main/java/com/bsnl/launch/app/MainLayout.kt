package com.bsnl.launch.app

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.base.utils.DisplayUtils
import com.bsnl.base.widget.CustomLayout
import com.bsnl.base.widget.matchParent
import com.bsnl.base.widget.onClick
import com.bsnl.common.utils.dp
import com.bsnl.sample.export.api.ISampleService
import com.bsnl.sample.export.path.SamplePath
import com.bsnl.sample.pkg.feature.view.countdowm.CountDownActivity
import com.bsnl.sample.pkg.feature.view.gson.GsonDemoActivity
import com.bsnl.sample.pkg.feature.view.viewpager.ViewPagerActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/21
 * @desc   :
 */
private val MARGIN: Int = 19.dp.toInt()

@SuppressLint("ResourceAsColor")
class MainLayout(context: Context, sampleService: ISampleService?) : CustomLayout(context) {


    init {
        setBackgroundColor(Color.parseColor("#11ff0000"))
    }

    val textStyleId = R.style.feature_launch_app_text
    private val toRightSideOffset = DisplayUtils.getScreenWidth() - MARGIN

    val listItem = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "ListView"

        onClick = {
            ARouter.getInstance().build(SamplePath.A_LISTVIEW_ACTIVITY).navigation()
        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 40.dp

        }
    }

    val webView = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "WebView"
        setBackgroundColor(Color.parseColor("#00BCD4"))
        onClick = {
            sampleService?.startWebViewActivity("https://www.wanandroid.com")
        }
        addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }

    val gson = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "Gson解析"
        onClick = {
            GsonDemoActivity.actionStart(context)
        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }

    val countdown = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "倒计时列表"
        onClick = {
            CountDownActivity.actionStart(context)
        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }

    val viewPager2 = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "ViewPager2"
        setBackgroundColor(Color.parseColor("#00BCD4"))
        onClick = {
            ViewPagerActivity.startAction(context)
        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }


    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEachAutoMeasure()
        setMeasuredDimension(measuredWidth, measuredHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        listItem.layout(0, 40.dp)
        webView.let { it.layout(listItem.left, listItem.bottom + it.marginTop) }
        countdown.let { it.layout(listItem.left, webView.bottom + it.marginTop) }
        viewPager2.let { it.layout(listItem.left, countdown.bottom + it.marginTop) }
    }
}


