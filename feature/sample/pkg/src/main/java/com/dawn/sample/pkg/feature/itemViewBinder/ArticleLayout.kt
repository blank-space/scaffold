package com.dawn.sample.pkg.feature.itemViewBinder

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.widget.CustomLayout
import com.dawn.base.widget.wrapContent
import com.dawn.base.utils.dp
import com.dawn.base.utils.onClick
import com.dawn.base.widget.matchParent
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.feature.view.FirstActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
private val MARGIN = 18.dp.toInt()
class ArticleLayout(context: Context,attrs: AttributeSet? = null) : CustomLayout(context,attrs) {

    init {
        onClick = {
            FirstActivity.actionStart(context)
        }
    }

    val tvTitle = AppCompatTextView(context).apply {
        text = ""
        maxLines= 1
        setTextColor(Color.parseColor("#262626"))
        ellipsize = TextUtils.TruncateAt.END
        val width = DisplayUtils.getScreenWidth() - (MARGIN * 2)
        this@ArticleLayout.addView(this, width, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
            topMargin = MARGIN / 2
        }
    }

    val tvAuthor = AppCompatTextView(context).apply {
        text = "作者："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
        }
    }

    val tvCategory = AppCompatTextView(context).apply {
        text = "分类："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
        }
    }

    val tvPublishTime = AppCompatTextView(context).apply {
        text = "时间："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
        }
    }

    private val space = View(context).apply {
        this@ArticleLayout.addView(this, matchParent, MARGIN/2)
    }


    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEachAutoMeasure()
        val h = tvTitle.measuredHeight + tvTitle.marginTop + tvAuthor.measuredHeight
        +tvAuthor.marginTop + space.measuredHeight
        setMeasuredDimension(measuredWidth, 150)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tvTitle.layout(MARGIN, MARGIN / 2)
        tvAuthor.let { it.layout(MARGIN, tvTitle.bottom + it.marginTop) }
        tvCategory.let { it.layout(tvAuthor.right + it.marginLeft, tvTitle.bottom + it.marginTop) }
        tvPublishTime.let { it.layout(tvCategory.right + it.marginLeft, tvTitle.bottom + it.marginTop) }
        space.let { it.layout(0,tvPublishTime.bottom) }
    }
}