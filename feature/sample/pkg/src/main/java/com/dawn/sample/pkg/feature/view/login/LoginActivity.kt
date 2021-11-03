package com.dawn.sample.pkg.feature.view.login

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import coil.load
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.utils.GlobalHandler
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.KeyboardUtils
import com.dawn.base.utils.startActivity
import com.dawn.base.viewmodel.StubViewModel
import com.dawn.base.widget.window.KeyboardStatePopupWindow
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityLoginBinding

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/4
 * @desc   :
 */
class LoginActivity : BaseActivity<StubViewModel,FeatureSamplePkgActivityLoginBinding>() {


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
    private val avatar = "https://gank.io/images/e0088b6b0773408bace28e102af9f8ee"

    override fun initView() {
        getTitleView()?.setTitleText(TAG)

        binding.etName.post {
            editTextTop = binding.etName.top
            editTextBot = binding.etName.bottom
        }

        binding.etName.viewTreeObserver?.addOnScrollChangedListener {
            scrollY = binding.etName.scaleY
        }

        binding.space.post {
            spaceViewOriginHeight = binding.space.height
        }
    }


    override fun initData() {
        binding.ivHeader.load(avatar)
    }

    override fun initListener() {
        super.initListener()
        binding.scrollView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                KeyboardUtils.hideSoftInput(binding.etName)
            }
            return@setOnTouchListener true
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        invisibleWindow = KeyboardStatePopupWindow(this, binding.etName)
        invisibleWindow?.setOnKeyboardStateChangerListener(object :
            KeyboardStatePopupWindow.OnKeyboardStateChangerListener {
            override fun onClose() {
                isSystemHandler = true
                GlobalHandler.postDelayed({ binding.scrollView.smoothScrollTo(0, 0) }, 100)

            }

            override fun onOpen(h: Int) {
                isSystemHandler = true
                if (needJust(h)) {
                    val offset = editTextBot - h
                    val lp = binding.space.layoutParams as ViewGroup.MarginLayoutParams

                    lp.height = offset * 2
                    binding.space.layoutParams = lp
                    binding.scrollView.postDelayed({
                        binding.scrollView.smoothScrollTo(0, offset / 2)
                    }, 100)
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

    override fun getLayout(): View? =null

}