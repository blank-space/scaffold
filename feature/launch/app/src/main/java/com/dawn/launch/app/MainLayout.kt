package com.dawn.launch.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.log.L
import com.dawn.base.permission.PermissionHelper
import com.dawn.base.utils.ActivitysManager
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.widget.CustomLayout
import com.dawn.base.widget.matchParent
import com.dawn.base.utils.dp
import com.dawn.base.utils.onClick
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.feature.view.countdowm.CountDownActivity
import com.dawn.sample.pkg.feature.view.download.DownLoadActivity
import com.dawn.sample.pkg.feature.view.gson.GsonDemoActivity
import com.dawn.sample.pkg.feature.view.location.FindLocationActivity
import com.dawn.sample.pkg.feature.view.viewpager.ViewPagerActivity
import java.lang.ref.WeakReference

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

    private val textStyleId = R.style.feature_launch_app_text
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
        text = "countdown"
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

    val location = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "FindLocation"
        onClick = {
            sampleService?.startFindLocationActivity()

        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }

    val search = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "search"
        setBackgroundColor(Color.parseColor("#00BCD4"))
        onClick = {
            sampleService?.startSearchActivity()

        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }

    val hexStatus = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "hexStatus"
        onClick = {
            (context as MainActivity).login()
            //  sampleService?.startHexStatusManagerActivity()

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
        location.let { it.layout(listItem.left, viewPager2.bottom + it.marginTop) }
        search.let { it.layout(listItem.left, location.bottom + it.marginTop) }
        hexStatus.let { it.layout(listItem.left, search.bottom + it.marginTop) }
    }
}


