package com.dawn.launch.app

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginTop
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.utils.dp
import com.dawn.base.utils.onClick
import com.dawn.base.widget.CustomLayout
import com.dawn.base.widget.matchParent
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.export.path.SamplePath

/*
 * @author : LeeZhaoXing
 * @date   : 2021/3/21
 * @desc   :
 */


private val MARGIN: Int = 19.dp.toInt()

@SuppressLint("ResourceAsColor")
class MainLayout (
    context: Context, sampleService: ISampleService?
)  : CustomLayout(context,null, 0) {
    init {
        setBackgroundColor(Color.parseColor("#ffffff"))
    }

    private val textStyleId = R.style.feature_launch_app_text
    private val toRightSideOffset = DisplayUtils.getScreenWidth() - MARGIN

    val android = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "原生"
        onClick = {
            ARouter.getInstance().build(SamplePath.A_LISTVIEW_ACTIVITY).navigation()
        }
        this@MainLayout.addView(this, matchParent, 60.dp) {
            topMargin = 40.dp
        }
    }

    val compose = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = "compose"
        setBackgroundColor(Color.parseColor("#00BCD4"))
        onClick = {

        }
        addView(this, matchParent, 60.dp) {
            topMargin = 10.dp
        }
    }

    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEachAutoMeasure()
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        android.layout(0, 40.dp)
        compose.let { it.layout(android.left, android.bottom + it.marginTop) }
    }
}


