package com.bsnl.launch.app

import android.Manifest
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bsnl.base.dsl.*
import com.bsnl.base.permission.PermissionHelper
import com.bsnl.base.utils.*
import com.bsnl.base.widget.ShowFps

import com.bsnl.common.iface.ViewState
import com.bsnl.common.page.base.BaseActivity
import com.bsnl.common.utils.getVm
import com.bsnl.constraint.export.api.ConstrainApi
import com.bsnl.sample.export.api.SampleApi
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   :
 */
class
MainActivity : BaseActivity<MainViewModel>() {

    private var weakReference: WeakReference<FragmentActivity>? = null
    private val coinUrl = "https://gank.io/images/e0088b6b0773408bace28e102af9f8ee"
    private lateinit var ivAvatar: ImageView

    private var tvFps: TextView? = null
    private var isOpenFpsMonitor = false

    private var mUrls = arrayListOf("https://m.toutiao.com/", "https://juejin.im/")
    private var count = 0


    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent

            TextView {
                layout_height = 40
                layout_width = match_parent
                textSize = 18f
                textStyle = bold
                text = "scaffold"
                gravity = gravity_center
                layout_id = "tv_title"
                top_toTopOf = parent_id
                background_color = "#ffffff"
                textColor = "#000000"
                start_toStartOf = parent_id

            }

            TextView {
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
                top_toBottomOf = "tv_title"
                margin_top = 10
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
                text = "ListItem Page"
                gravity = gravity_center
                layout_id = "tv_list_page"
                onClick = {
                    ApiUtils.getApi(SampleApi::class.java).startSampleActivity(this@MainActivity)
                }
                top_toBottomOf = "iv_show"
                start_toStartOf = parent_id
                background_color = "#eeeeee"

            }


            tvFps = TextView {
                layout_id = "tv_open_fps"
                layout_height = 40
                layout_width = match_parent
                margin_top = 10
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                background_color = "#eeeeee"
                top_toBottomOf = "tv_list_page"
                start_toStartOf = parent_id
                text = "Hide FpsMonitor"
                onClick = {
                    ShowFps.setVisibility(isShow = false)
                }
            }

            TextView {
                layout_id = "tv_web"
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                background_color = "#eeeeee"
                top_toBottomOf = "tv_open_fps"
                start_toStartOf = parent_id
                text = "webView"
                onClick = {
                    ApiUtils.getApi(SampleApi::class.java).startWebViewActivity(context, mUrls[0])
                }
                margin_top = 10
            }

            TextView {
                layout_id = "tv_login"
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                background_color = "#eeeeee"
                top_toBottomOf = "tv_web"
                start_toStartOf = parent_id
                text = "log in"
                onClick = {
                    ApiUtils.getApi(SampleApi::class.java).startLoginActivity(this@MainActivity)
                }
                margin_top = 10
            }



            TextView {
                layout_id = "tv_viewpager"
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                background_color = "#eeeeee"
                top_toBottomOf = "tv_login"
                start_toStartOf = parent_id
                text = "viewPager"
                onClick = {
                    ApiUtils.getApi(SampleApi::class.java).startViewPagerActivity(this@MainActivity)
                }
                margin_top = 10
            }



            TextView {
                layout_id = "tv_async"
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                background_color = "#eeeeee"
                top_toBottomOf = "tv_viewpager"
                start_toStartOf = parent_id
                text = "异步创建View"
                onClick = {
                    ApiUtils.getApi(SampleApi::class.java).startAsyncCreateViewActivity(this@MainActivity)
                }
                margin_top = 10
            }


            TextView {
                layout_id = "tv_constraint"
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                background_color = "#eeeeee"
                top_toBottomOf = "tv_async"
                start_toStartOf = parent_id
                text = "ConstraintLayout"
                onClick = {
                    ApiUtils.getApi(ConstrainApi::class.java).startSampleActivity(this@MainActivity)
                }
                margin_top = 10
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

    override fun initViewModel(): MainViewModel = getVm()

    override fun initData() {
        setState(ViewState.STATE_LOADING)
        //模拟请求
        ivAvatar.postDelayed({ setState(ViewState.STATE_COMPLETED) }, 200)

    }

    override fun getLayoutId(): Int = 0


}