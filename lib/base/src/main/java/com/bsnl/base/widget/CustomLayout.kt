package com.bsnl.base.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/21
 * @desc   :
 */
abstract class CustomLayout (context: Context) : ViewGroup(context) {

    protected val View.measuredHeightWithMargins get() = (measuredHeight + marginTop + marginBottom)

    protected val View.measuredWidthWithMargins get() = (measuredWidth + marginLeft + marginRight)

    protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
        if (!fromRight) {
            layout(x, y, x + measuredWidth, y + measuredHeight)
        } else {
            layout(this@CustomLayout.measuredWidth - x - measuredWidth, y)
        }
    }

    protected fun View.autoMeasure() {
        measure(
            defaultWidthMeasureSpec(this@CustomLayout),
            defaultHeightMeasureSpec(this@CustomLayout)
        )
    }

    protected fun View.defaultWidthMeasureSpec(parent: ViewGroup): Int {
        return when (layoutParams.width) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parent.measuredWidth.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> parent.measuredWidth.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    protected fun View.defaultHeightMeasureSpec(parent: ViewGroup): Int {
        return when (layoutParams.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parent.measuredHeight.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> parent.measuredHeight.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    protected fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY);
    }

    protected fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST);
    }

    protected class LayoutParams(w: Int, h: Int) : MarginLayoutParams(w, h)
}