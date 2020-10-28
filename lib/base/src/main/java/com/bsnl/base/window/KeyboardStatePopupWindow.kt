package com.bsnl.base.window

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import com.bsnl.base.utils.DisplayUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/28
 * @desc   : 获取软键盘开启状态、高度.
 *
 * note:需要宿主Activity设置windowSoftInputMode= adjustNothing
 */
class KeyboardStatePopupWindow(var context: Context, anchorView: View) : PopupWindow(),
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

        anchorView.post {
            showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                0,
                0
            )
        }
    }

    private var mMaxHeight = 0
    private var isOpenKeyboard = false
    private var onKeyboardStateChangeListener: OnKeyboardStateChangerListener? = null

    override fun onGlobalLayout() {
        val rect = Rect()
        contentView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > mMaxHeight) {
            mMaxHeight = rect.bottom
        }

        val screenHeight: Int = DisplayUtils.getScreenHeight(context)
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

    fun release() {
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    fun setOnKeyboardStateChangerListener(listener: OnKeyboardStateChangerListener) {
        this.onKeyboardStateChangeListener = listener
    }

    interface OnKeyboardStateChangerListener {
        fun onOpen(h: Int)
        fun onClose()
    }

}