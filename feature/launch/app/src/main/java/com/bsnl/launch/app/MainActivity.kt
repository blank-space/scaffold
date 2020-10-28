package com.bsnl.launch.app

import android.Manifest
import android.graphics.Rect
import android.os.Build
import android.text.Selection
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.bsnl.base.BaseApp
import com.bsnl.base.dsl.*
import com.bsnl.base.log.L
import com.bsnl.base.permission.PermissionHelper
import com.bsnl.base.utils.*
import com.bsnl.base.window.KeyboardStatePopupWindow
import com.bsnl.common.dataBinding.DataBindingActivity
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.iface.ViewState
import com.bsnl.common.utils.getVm
import com.bsnl.sample.export.api.SampleApi
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class MainActivity : DataBindingActivity<MainViewModel>() {


    private var weakReference: WeakReference<FragmentActivity>? = null
    private val coinUrl = "https://gank.io/images/b140f015a16e444aad6d76262f676a78"
    private lateinit var ivAvatar: ImageView
    private var mEt: EditText? = null
    private var editTextTop = 0
    private var editTextBot = 0
    private var invisibleWindow: KeyboardStatePopupWindow? = null
    private var scrollY = 0f

    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent

            TextView {
                margin_top = 10
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                text = "请求权限"
                gravity = gravity_center
                layout_id = "tv_permission"
                onClick = {
                    PermissionHelper.request(
                        activity = weakReference!!,
                        permission = mutableListOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    )
                }
                top_toTopOf = parent_id
                start_toStartOf = parent_id
                background_color = "#eeeeee"

            }


            TextView {
                layout_height = 40
                layout_width = match_parent
                margin_top = 10
                textSize = 16f
                textStyle = bold
                text = "显示图片"
                gravity = gravity_center
                layout_id = "tv_img"
                onClick = {
                    ivAvatar.visibility = android.view.View.VISIBLE
                }
                top_toBottomOf = "tv_permission"
                start_toStartOf = parent_id
                background_color = "#eeeeee"
            }


            ivAvatar = ImageView {
                layout_id = "iv_show"
                layout_width = 180
                layout_height = 180
                align_horizontal_to = parent_id
                top_toBottomOf = "tv_img"
                visibility = gone
                margin_top = 10
            }

            TextView {
                layout_height = 40
                layout_width = match_parent
                margin_top = 10
                textSize = 16f
                textStyle = bold
                text = "跳转到其他页面"
                gravity = gravity_center
                layout_id = "iv_start_activity"
                onClick = {
                    ApiUtils.getApi(SampleApi::class.java).startSampleActivity(this@MainActivity)
                }
                top_toBottomOf = "iv_show"
                start_toStartOf = parent_id
                background_color = "#eeeeee"

            }

            mEt = EditText {
                layout_height = 300
                layout_width = match_parent
                margin_top = 200
                gravity = Gravity.TOP
                textSize = 16f
                top_toBottomOf = "iv_start_activity"
                start_toStartOf = parent_id

            }

        }
    }


    override fun initView() {
        weakReference = WeakReference(this)
        ivAvatar.load {
            this.url = coinUrl
            this.isCircle = true
        }
    }

    override fun getLayout(): View? {
        return rootView
    }

    override fun getLayoutId() = 0

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? {
        return null
    }

    override fun initViewModel(): MainViewModel = getVm()

    override fun initData() {
        showLoading()
        //模拟请求
        ivAvatar.postDelayed({ setState(ViewState.STATE_COMPLETED) }, 500)
        mEt?.setText(
            """
1. 熟练掌握 Java，包括注解、反射、泛型、异常等相关知识，熟悉其在 JVM 的实现原理
2. 熟悉 Java/Android 中常见的集合源码，包括 List、Set、Map、Queue/Deque 等
3. 对 Java 并发有一定理解，熟悉 synchronized、volatile、原子类等实现原理
4. 熟悉 JVM 相关知识，包括内存区域、内存模型、GC、类加载机制、编译优化等
5. 熟练掌握 Android 应用层开发相关知识，熟悉四大组件、动画的使用
6. 熟悉 View 相关体系，包括 View 绘制流程、事件分发、刷新机制
7. 熟悉 Binder 进程间通信机制，熟悉其通信模型以及完整的通信流程
8. 熟悉 Android 的系统启动流程，Activity、Service 启动流程、Handler 消息机制、SP 源码等
9. 熟悉 Gradle 相关知识，包括自定义 Task、编译打包流程、自定义 Gradle Plugin、编译优化等
10. 熟悉插件化的实现原理，静态代理式和 Hook 式；了解热修复的实现原理
11. 了解常见的性能优化手段，做过包体积优化、布局优化、内存优化等
12. 熟悉计算机网络相关协议，包括 TCP/IP、HTTP/1.x、HTTP2、HTTPS 等
        """
        )

        mEt?.post {

            editTextTop = mEt!!.top
            editTextBot = mEt!!.bottom
        }

        mEt?.viewTreeObserver?.addOnScrollChangedListener(object :
            ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                scrollY = mEt?.scaleY!!
            }
        })
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        invisibleWindow = KeyboardStatePopupWindow(this, ivAvatar)
        invisibleWindow?.setOnKeyboardStateChangerListener(object :
            KeyboardStatePopupWindow.OnKeyboardStateChangerListener {
            override fun onClose() {
            }

            override fun onOpen(h: Int) {
                //光标在EditText中的位置
                val currentCursorY = getCurrentCursorY(mEt!!)
                //光标在屏幕中的位置
                val realY = currentCursorY - scrollY + editTextTop
                //偏移量
                val offset = (realY + h - DisplayUtils.getScreenHeight(BaseApp.application)).toInt()
                //光标在软键盘底下,让文字向上位移
                if (offset > 0) {
                    mEt?.scrollY = offset

                }
            }
        })
    }

    /**
     * 获取当前光标的Y轴坐标
     */
    fun getCurrentCursorY(editText: EditText): Int {
        val selectionStart = Selection.getSelectionStart(editText.text)
        val layout = editText.layout
        val bound = Rect()
        val line = layout.getLineForOffset(selectionStart)
        layout.getLineBounds(line, bound)
        return bound.bottom
    }


}