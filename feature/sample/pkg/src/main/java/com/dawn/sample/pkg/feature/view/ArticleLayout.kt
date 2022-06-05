package com.dawn.sample.pkg.feature.view

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
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

class ArticleLayout(context: Context) : CustomLayout(context) {

    val tvTitle = AppCompatTextView(context).apply {
        text = ""
        maxLines = 1
        textSize = 16f
        setTextColor(Color.parseColor("#262626"))
        ellipsize = TextUtils.TruncateAt.END
        val width = DisplayUtils.getScreenWidth() - (MARGIN * 2) - 24.dp
        this@ArticleLayout.addView(this, width, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN + 24.dp
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
            topMargin = MARGIN
        }
    }

    val tvCategory = AppCompatTextView(context).apply {
        text = "分类："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
            topMargin = MARGIN
        }
    }

    val tvPublishTime = AppCompatTextView(context).apply {
        text = "时间："
        setTextColor(Color.parseColor("#666666"))
        textSize = 10f
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            leftMargin = MARGIN
            rightMargin = MARGIN
            topMargin = MARGIN
        }
    }

    val ivFavorite = ImageView(context).apply {
        setImageResource(R.drawable.ic_baseline_favorite_border_24)
        this@ArticleLayout.addView(this, wrapContent, wrapContent) {
            rightMargin = MARGIN
        }
    }

    private val space = View(context).apply {
        this@ArticleLayout.addView(this, matchParent, MARGIN / 2)
    }


    override fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEachAutoMeasure()
        val h = (tvTitle.measuredHeight + tvTitle.marginTop + tvAuthor.measuredHeight
                + tvAuthor.marginTop + space.measuredHeight)
        setMeasuredDimension(measuredWidth, h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        tvTitle.layout(
            tvTitle.marginLeft,
            tvTitle.marginTop,
            this.measuredWidth - tvTitle.marginRight,
            tvTitle.measuredHeight + tvTitle.marginTop
        )
        tvAuthor.let { it.layout(MARGIN, tvTitle.bottom + it.marginTop) }
        tvCategory.let { it.layout(tvAuthor.right + it.marginLeft, tvTitle.bottom + it.marginTop) }
        tvPublishTime.let {
            it.layout(
                tvCategory.right + it.marginLeft,
                tvTitle.bottom + it.marginTop
            )
        }
        space.layout(0, tvPublishTime.bottom)
        ivFavorite.let { it.layout(it.marginRight, (this.height - it.measuredHeight) / 2, true) }
    }
}