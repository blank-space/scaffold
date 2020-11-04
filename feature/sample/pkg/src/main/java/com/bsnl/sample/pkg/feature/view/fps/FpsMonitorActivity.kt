package com.bsnl.sample.pkg.feature.view.fps

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.base.dsl.*
import com.bsnl.base.utils.FpsMonitor
import com.bsnl.common.utils.startActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/4
 * @desc   :
 */
class FpsMonitorActivity : AppCompatActivity() {

    private var tvFps: TextView? = null

    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent


            TextView {

                layout_id = "tv_open_fps"
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                top_toTopOf = parent_id
                start_toStartOf = parent_id
                text = "开启FPS检测"
                onClick = {
                    FpsMonitor.startMonitor { fps ->
                        tvFps?.setText("fps:$fps")
                    }
                }
                background_color = "#eeeeee"
                //margin_top 不能写在设置宽高之前，此时layoutParams=null
                margin_top = 10
            }

            TextView {
                layout_id = "tv_close_fps"

                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                top_toBottomOf = "tv_open_fps"
                start_toStartOf = parent_id
                text = "关闭FPS检测"
                onClick = {
                    tvFps?.setText("")
                    FpsMonitor.stopMonitor()
                }
                background_color = "#eeeeee"
                margin_top = 10
            }

            tvFps = TextView {
                layout_id = "tv_fps"
                layout_height = 40
                layout_width = 80
                textSize = 16f
                textStyle = bold
                gravity = gravity_center
                top_toTopOf = parent_id
                start_toStartOf = parent_id
                textColor = "#ff0000"
                text = ""

            }
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
    }

    companion object {
        fun startAction(context: Context) {
            startActivity<FpsMonitorActivity>(context)
        }
    }
}