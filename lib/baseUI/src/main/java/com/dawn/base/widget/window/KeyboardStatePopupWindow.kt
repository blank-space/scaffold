package com.dawn.base.widget.window

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.PopupWindow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/28
 * @desc   : 获取软键盘开启状态、高度.
 *
 * note:需要宿主Activity设置windowSoftInputMode= adjustNothing
 */
class KeyboardStatePopupWindow(var context: ContextWrapper, anchorView: View) : PopupWindow(),
    ViewTreeObserver.OnGlobalLayoutListener {

    /**
     * 设置一个宽度为0，高度等于屏幕高度的完全不可见的window，设置其softInputMode
     * 为adjustResize，当这个Window被挤压的时候，就能判断计算出键盘的状态，进而通知到activity
     */
    init {
        val contentView = View(context)
        setContentView(contentView)
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
        contentView.viewTreeObserver.addOnGlobalLayoutListener(this)

        if (findActivity(context)!=null) {
            if (isActivityRunning(context as Activity)) {
                anchorView.post {
                    showAtLocation(
                        anchorView,
                        Gravity.NO_GRAVITY,
                        0,
                        0
                    )
                }
            }
        }
    }

    private var mMaxHeight = 0
    private var isOpenKeyboard = false
    private var onKeyboardStateChangeListener: OnKeyboardStateChangerListener? = null

    /**
     * 根据Context找到对应的Activity
     */
    private fun findActivity(context: Context): Activity? {
        if (context is Activity) {
            return context
        }

        return if (context is ContextWrapper) {
            val wrapper = context
            findActivity(wrapper.baseContext);
        } else {
            null
        }
    }


    /**
     * activity是否在运行
     */
    private fun isActivityRunning(activity: Activity): Boolean {
        return if (activity.isDestroyed) {
            false
        } else !activity.isFinishing
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        contentView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > mMaxHeight) {
            mMaxHeight = rect.bottom
        }

        val screenHeight: Int = getScreenHeight()
        val keyboardHeight = mMaxHeight - rect.bottom
        //键盘是否可见
        val visibility = keyboardHeight > screenHeight / 4

        if (!isOpenKeyboard && visibility) {
            isOpenKeyboard = true
            //键盘弹出了
            onKeyboardStateChangeListener?.onOpen(keyboardHeight)
        } else if (isOpenKeyboard && !visibility) {
            isOpenKeyboard = false
            //键盘关闭了
            onKeyboardStateChangeListener?.onClose()
        }

    }

    fun getScreenHeight(): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    fun release() {
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        onKeyboardStateChangeListener = null
    }

    fun setOnKeyboardStateChangerListener(listener: OnKeyboardStateChangerListener?) {
        this.onKeyboardStateChangeListener = listener
    }

    interface OnKeyboardStateChangerListener {
        fun onOpen(h: Int)
        fun onClose()
    }

}