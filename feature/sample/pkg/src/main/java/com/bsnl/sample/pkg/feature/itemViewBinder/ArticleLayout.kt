package com.bsnl.sample.pkg.feature.itemViewBinder

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.bsnl.base.log.L
import com.bsnl.base.utils.DisplayUtils
import com.bsnl.base.widget.CustomLayout
import com.bsnl.base.widget.matchParent
import com.bsnl.base.widget.wrapContent
import com.bsnl.common.utils.dp
import com.bsnl.sample.pkg.R

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
private val MARGIN = 18.dp.toInt()

class ArticleLayout(context: Context,attrs: AttributeSet? = null) : CustomLayout(context,attrs) {
    private val textStyleId = R.style.lib_base_article_text


    val tvTitle = AppCompatTextView(ContextThemeWrapper(context, textStyleId)).apply {
        text = ""
        maxLines= 1
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
            bottomMargin = MARGIN / 2
        }
    }

    val tvCategory = AppCompatTextView(context).apply {
        text = "分类："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
            bottomMargin = MARGIN / 2
        }
    }

    val tvPublishTime = AppCompatTextView(context).apply {
        text = "时间："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
            bottomMargin = MARGIN / 2
        }
    }


    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEachAutoMeasure()
        val h = tvTitle.measuredHeight + tvTitle.marginTop + tvAuthor.measuredHeight
        +tvAuthor.marginTop + tvAuthor.marginBottom
        setMeasuredDimension(measuredWidth, h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tvTitle.layout(MARGIN, MARGIN / 2)
        tvAuthor.let { it.layout(MARGIN, tvTitle.bottom + it.marginTop) }
        tvCategory.let { it.layout(tvAuthor.right + it.marginLeft, tvTitle.bottom + it.marginTop) }
        tvPublishTime.let { it.layout(tvCategory.right + it.marginLeft, tvTitle.bottom + it.marginTop) }
    }
}