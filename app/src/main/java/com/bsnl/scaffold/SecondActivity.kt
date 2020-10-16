package com.bsnl.scaffold

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.bsnl.base.dsl.*
import com.bsnl.base.imageloader.ILoadBitmapListener
import com.bsnl.base.imageloader.glide.ImageConfigImpl
import com.bsnl.base.log.L
import com.bsnl.base.manager.KeyboardStateManager
import com.bsnl.base.net.ServiceCreator
import com.bsnl.base.permission.PermissionHelper
import com.bsnl.base.utils.clearLoadBitmapListener
import com.bsnl.base.utils.getBitmapByGlide
import com.bsnl.scaffold.event.DemoEvent
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
class SecondActivity : BaseActivity() {

    private var weakReference: WeakReference<FragmentActivity>? = null
    private val coinUrl = "https://gank.io/images/b140f015a16e444aad6d76262f676a78"
    private var ivAvatar: ImageView? = null
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
                        needExplainReason = true
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
                    ivAvatar!!.visibility = android.view.View.VISIBLE
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
                onClick = {
                    ivAvatar!!.visibility = android.view.View.VISIBLE
                }
                top_toBottomOf = "iv_show"
                start_toStartOf = parent_id
                background_color = "#eeeeee"
                onClick = {
                    //使用协程切换线程
                    CoroutineScope(Dispatchers.IO).launch {
                        wanAndroidService.login("lizhao", "123457")
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
                    startActivity(Intent(this@SecondActivity, ThirdActivity::class.java))
                }
                top_toBottomOf = "tv_retry_when_error"
                start_toStartOf = parent_id
                background_color = "#eeeeee"

            }


        }
    }


    private var callback: ILoadBitmapListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weakReference = WeakReference(this)
        setContentView(rootView)

        lifecycle.addObserver(KeyboardStateManager)
        //写法3
        getBitmapByGlide(ImageConfigImpl().also {
            it.url = coinUrl
            it.isCircle = true
            it.imageView = ivAvatar
        }, callback = object : ILoadBitmapListener {
            override fun onGetBitmap(bitmap: Bitmap) {
                ivAvatar!!.setImageBitmap(bitmap)
            }

        })


        LiveEventBus.get(DemoEvent::class.java)
            .observe(this, Observer {
                L.d("我在这里")

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        callback = null
        clearLoadBitmapListener()
        super.onDestroy()
    }
}