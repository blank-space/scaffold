package com.bsnl.launch.app

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bsnl.base.dsl.*
import com.bsnl.base.net.ServiceCreator
import com.bsnl.base.permission.PermissionHelper
import com.bsnl.base.utils.ApiUtils
import com.bsnl.base.utils.load
import com.bsnl.common.dataBinding.DataBindingActivity
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.iface.ViewState
import com.bsnl.common.utils.getVm
import com.bsnl.sample.export.api.SampleApi
import kotlinx.android.synthetic.main.feature_launch_app_activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

            EditText {
                layout_height = 40
                layout_width = match_parent
                margin_top = 10
                textSize = 16f
                top_toBottomOf = "iv_start_activity"
                start_toStartOf = parent_id

            }

        }
    }


    override fun initView() {
        weakReference = WeakReference(this)
        ivAvatar.load {
            this.url =coinUrl
            this.isCircle =true
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
        ivAvatar.postDelayed({ setState(ViewState.STATE_COMPLETED) }, 2000)

    }

}