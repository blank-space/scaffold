package com.bsnl.scaffold

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.bsnl.base.dsl.*
import com.bsnl.base.net.ServiceCreator
import com.bsnl.base.permission.PermissionHelper
import com.bsnl.base.utils.load
import com.bsnl.base.utils.showToast
import com.bsnl.scaffold.event.DemoEvent
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class MainActivity : BaseActivity() {

    private var weakReference: WeakReference<FragmentActivity>? = null
    private val coinUrl = "https://gank.io/images/b140f015a16e444aad6d76262f676a78"
    private lateinit var ivAvatar: ImageView
    private val wanAndroidService = ServiceCreator.create<TestService>()

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
                text = "请求网络"
                gravity = gravity_center
                layout_id = "tv_retry_when_error"
                top_toBottomOf = "iv_show"
                start_toStartOf = parent_id
                background_color = "#eeeeee"
                onClick = {
                    //使用协程切换线程
                    CoroutineScope(Dispatchers.IO).launch {
                        ServiceCreator.create<TestService>().login("lizhao", "123457")
                    }
                }
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
                    //shareViewModel!!.sCount.value = 2
                    startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                }
                top_toBottomOf = "tv_retry_when_error"
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weakReference = WeakReference(this)
        setContentView(rootView)


        ivAvatar.load {
            this.url = coinUrl
            this.isCircle = true
        }



        LiveEventBus.get(DemoEvent::class.java).observe(this, Observer {
            it.content.showToast()
        })


    }
}