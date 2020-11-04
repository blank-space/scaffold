package com.bsnl.base.widget

import android.content.res.Configuration
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import com.bsnl.base.BaseApp
import com.bsnl.base.R
import com.bsnl.base.utils.DisplayUtils
import com.bsnl.base.utils.FpsMonitor
import com.bsnl.base.utils.MMKVUtil
import com.bsnl.base.utils.TouchUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/4
 * @desc   : 显示App当前的Fps，[FpsMonitor]]
 */
class ShowFps : RelativeLayout(BaseApp.application) {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        wrapPosition()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        savePosition()
    }

    private fun savePosition() {
        saveViewX(this, x.toInt())
        saveViewY(this, y.toInt())
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        wrapPosition()
    }

    private fun wrapPosition() {
        post(Runnable {
            x = getViewX(this@ShowFps).toFloat()
            y = getViewY(this@ShowFps, DisplayUtils.getScreenHeight() / 3).toFloat()
        })
    }


    companion object {
        val instance: ShowFps? = ShowFps()

        fun setVisibility(isShow: Boolean) {
            if (instance == null) return
            instance.visibility = if (isShow) View.VISIBLE else View.GONE
        }

    }

    init {
        val view = View.inflate(context, R.layout.lib_base_fps_icon, this)

        val tvFps = view.findViewById<TextView>(R.id.tvFps)
        FpsMonitor.startMonitor { fps ->
            tvFps?.setText("fps:$fps")
        }

        TouchUtils.setOnTouchListener(this, object : TouchUtils.OnTouchUtilsListener() {
            private var rootViewWidth = 0
            private var rootViewHeight = 0
            private var viewWidth = 0
            private var viewHeight = 0
            private var statusBarHeight = 0


            override fun onDown(
                view: View,
                x: Int,
                y: Int,
                event: MotionEvent?
            ): Boolean {
                viewWidth = view.width
                viewHeight = view.height
                val contentView = view.rootView.findViewById<View>(R.id.content)
                contentView?.let {
                    rootViewWidth = it.width
                    rootViewHeight = it.height
                }
                if (rootViewWidth == 0) {
                    rootViewWidth = DisplayUtils.getScreenWidth()
                    rootViewHeight = DisplayUtils.getScreenHeight()
                }
                statusBarHeight = DisplayUtils.getStatusHeight()
                processScale(view, true)
                return true
            }

            override fun onMove(
                view: View,
                direction: Int,
                x: Int,
                y: Int,
                dx: Int,
                dy: Int,
                totalX: Int,
                totalY: Int,
                event: MotionEvent?
            ): Boolean {
                view.x = Math.min(
                    Math.max(0f, view.x + dx),
                    rootViewWidth - viewWidth.toFloat()
                )
                view.y = Math.min(
                    Math.max(
                        statusBarHeight.toFloat(),
                        view.y + dy
                    ), rootViewHeight - viewHeight.toFloat()
                )
                return true
            }

            override fun onStop(
                view: View,
                direction: Int,
                x: Int,
                y: Int,
                totalX: Int,
                totalY: Int,
                vx: Int,
                vy: Int,
                event: MotionEvent?
            ): Boolean {
                stick2HorizontalSide(view)
                processScale(view, false)
                return true
            }

            private fun stick2HorizontalSide(view: View) {
                view.animate()
                    .setInterpolator(DecelerateInterpolator())
                    .translationX(if (view.x + viewWidth / 2f > rootViewWidth / 2f) (rootViewWidth - viewWidth).toFloat() else 0.toFloat())
                    .setDuration(100)
                    .withEndAction { savePosition() }
                    .start()
            }

            private fun processScale(view: View, isDown: Boolean) {
                val value: Float = if (isDown) 1 - 0.1f else 1F
                view.animate()
                    .scaleX(value)
                    .scaleY(value)
                    .setDuration(100)
                    .start()
            }
        })
        setOnClickListener {
            if (FpsMonitor.isOpen()) {
                FpsMonitor.stopMonitor()
                tvFps.setText("show fps")
            } else {
                FpsMonitor.startMonitor { fps ->
                    tvFps?.setText("fps:$fps")
                }
            }

        }
    }

    private fun saveViewY(view: View, y: Int) {
        if (DisplayUtils.isPortrait()) {
            MMKVUtil.put(view.javaClass.simpleName + ".yP", y)
        } else {
            MMKVUtil.put(view.javaClass.simpleName + ".yL", y)
        }
    }

    private fun saveViewX(view: View, x: Int) {
        if (DisplayUtils.isPortrait()) {
            MMKVUtil.put(view.javaClass.simpleName + ".xP", x)
        } else {
            MMKVUtil.put(view.javaClass.simpleName + ".xL", x)
        }
    }

    private fun getViewX(view: View): Int {
        return if (DisplayUtils.isPortrait()) {
            MMKVUtil.getInt(view.javaClass.simpleName + ".xP")
        } else {
            MMKVUtil.getInt(view.javaClass.simpleName + ".xL")
        }
    }


    private fun getViewY(view: View, defaultVal: Int): Int {
        return if (DisplayUtils.isPortrait()) {
            MMKVUtil.getInt(view.javaClass.simpleName + ".yP", defaultVal)
        } else {
            MMKVUtil.getInt(view.javaClass.simpleName + ".yL", defaultVal)
        }
    }
}