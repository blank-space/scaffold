package com.dawn.base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import androidx.core.view.*

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/21
 * @desc   :
 */
abstract class CustomLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    protected val View.measuredWidthWithMargins get() = (measuredWidth + marginLeft + marginRight)
    protected val View.measuredHeightWithMargins get() = (measuredHeight + marginTop + marginBottom)
    protected val Int.dp: Int get() = (this * resources.displayMetrics.density + 0.5f).toInt()

    class LayoutParams(width: Int, height: Int) : MarginLayoutParams(width, height)

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(matchParent, wrapContent)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        this.onMeasureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    protected abstract fun onMeasureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int)

    protected fun View.autoMeasure() {
        if (isGone) return
        measure(
            this.defaultWidthMeasureSpec(parentView = this@CustomLayout),
            this.defaultHeightMeasureSpec(parentView = this@CustomLayout)
        )
    }

    protected fun View.forEachAutoMeasure() {
        forEach { it.autoMeasure() }
    }

    protected fun View.layout(x: Int, y: Int, fromRight: Boolean = false) {
        if (isGone) return
        if (!fromRight) {
            layout(x, y, x + measuredWidth, y + measuredHeight)
        } else {
            layout(this@CustomLayout.measuredWidth - x - measuredWidth, y)
        }
    }

    /**
     * layoutParams.width 是开发者设置的值，例如200dp。在一般场景下是在xml文件被inflact的时候，生成LayoutParams
     * @return Int: widthMeasureSpec
     */
    protected fun View.defaultWidthMeasureSpec(parentView: ViewGroup): Int {
        return when (layoutParams.width) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredWidth.toExactlyMeasureSpec()
            //设置子view有多宽就多宽
            ViewGroup.LayoutParams.WRAP_CONTENT -> Int.MAX_VALUE.toAtMostMeasureSpec()
            //对应UNSPECIFIED测量模式，通用方法无法处理该情况
            0 -> throw IllegalAccessException("Need special treatment for $this")
            //精确大小
            else -> layoutParams.width.toExactlyMeasureSpec()
        }
    }

    protected fun View.defaultHeightMeasureSpec(parentView: ViewGroup): Int {
        return when (layoutParams.height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> parentView.measuredHeight.toExactlyMeasureSpec()
            ViewGroup.LayoutParams.WRAP_CONTENT -> Int.MAX_VALUE.toAtMostMeasureSpec()
            0 -> throw IllegalAccessException("Need special treatment for $this")
            else -> layoutParams.height.toExactlyMeasureSpec()
        }
    }

    protected fun Int.toExactlyMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.EXACTLY)
    }

    protected fun Int.toAtMostMeasureSpec(): Int {
        return MeasureSpec.makeMeasureSpec(this, MeasureSpec.AT_MOST)
    }

    fun addView(child: View, width: Int, height: Int, apply: (LayoutParams.() -> Unit)) {
        val params = generateDefaultLayoutParams()
        params.width = width
        params.height = height
        params.apply { apply.invoke(this) }
        super.addView(child, params)
    }

    fun View.overScrollNever() {
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    fun ViewGroup.horizontalCenterX(child: View): Int {
        return (measuredWidth - child.measuredWidth) / 2
    }

    fun ViewGroup.verticalCenterTop(child: View): Int {
        return (measuredHeight - child.measuredHeight) / 2
    }
}

const val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
const val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

fun View.transparentBackground() {
    setBackgroundColor(Color.TRANSPARENT)
}

val View.parentView get() = parent as ViewGroup

fun View?.performHapticFeedbackSafely() {
    try {
        this?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    } catch (t: Throwable) {
        t.message?.let { Log.e("@@", it) }
    }
}

