package com.bsnl.sample.pkg.feature.view.fps

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Selection
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.bsnl.base.dsl.dp
import com.bsnl.base.log.L
import com.bsnl.base.utils.DisplayUtils
import com.bsnl.base.utils.GlobalHandler
import com.bsnl.base.utils.KeyboardUtils
import com.bsnl.base.utils.load
import com.bsnl.base.window.KeyboardStatePopupWindow
import com.bsnl.common.dataBinding.DataBindingActivity
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.ui.titlebar.ToolbarTitleView
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.common.viewmodel.StubViewModel
import com.bsnl.sample.pkg.R
import kotlinx.android.synthetic.main.feature_sample_pkg_activity_login.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/4
 * @desc   :
 */
class LoginActivity : DataBindingActivity<StubViewModel>() {


    companion object {
        fun startAction(context: Context) {
            startActivity<LoginActivity>(context)
        }
    }

    private var invisibleWindow: KeyboardStatePopupWindow? = null
    private var editTextTop = 0
    private var editTextBot = 0
    private var scrollY = 0f
    private var spaceViewOriginHeight = 0
    private var isSystemHandler = false
    private val avatar = "https://gank.io/images/b140f015a16e444aad6d76262f676a78"

    override fun initView() {
        mTitleView = findViewById<ToolbarTitleView>(R.id.title)
        mTitleView?.setTitleText("Log In")

        et_name?.post {
            editTextTop = et_name!!.top
            editTextBot = et_name!!.bottom
        }

        et_name?.viewTreeObserver?.addOnScrollChangedListener(object :
            ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                scrollY = et_name?.scaleY!!
            }
        })

        space.post {
            spaceViewOriginHeight = space.height
        }
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_activity_login

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? = null

    override fun initViewModel(): StubViewModel = getVm()

    override fun initData() {
        iv_header.load {
            this.url =avatar
        }
    }

    override fun initListener() {
        super.initListener()
        scrollView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                KeyboardUtils.hideSoftInput(et_name)
            }
            return@setOnTouchListener true
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        invisibleWindow = KeyboardStatePopupWindow(this, et_name)
        invisibleWindow?.setOnKeyboardStateChangerListener(object :
            KeyboardStatePopupWindow.OnKeyboardStateChangerListener {
            override fun onClose() {
                isSystemHandler = true
                GlobalHandler.postDelayed(Runnable {    scrollView.smoothScrollTo(0, 0)  },100)

            }

            override fun onOpen(h: Int) {
                isSystemHandler = true
                if (needJust(h)) {
                    val offset = editTextBot - h
                    val lp = space.layoutParams as ViewGroup.MarginLayoutParams
                    if (lp != null) {
                        lp.height = offset * 2
                        space.layoutParams = lp
                        scrollView.postDelayed({
                            scrollView.smoothScrollTo(0, offset / 2)
                        }, 100)

                    }
                }
            }
        })
    }

    /**
     * editText底部的位置+键盘高度之和是否大于屏幕高度，是则需要调整EditText的位置
     */
    private fun needJust(h: Int): Boolean {
        return editTextBot + h > DisplayUtils.getScreenHeight()
    }


    override fun onDestroy() {
        invisibleWindow?.release()
        invisibleWindow = null
        super.onDestroy()

    }

}