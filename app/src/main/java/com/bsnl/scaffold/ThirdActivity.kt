package com.bsnl.scaffold

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bsnl.base.dsl.*
import com.bsnl.scaffold.event.DemoEvent
import com.jeremyliao.liveeventbus.LiveEventBus
import java.lang.ref.WeakReference


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
class ThirdActivity : BaseActivity() {

    private var weakReference: WeakReference<FragmentActivity>? = null


    private var tv: TextView? = null


    private val rootView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height = match_parent

            tv = TextView {
                margin_top = 10
                layout_height = 40
                layout_width = match_parent
                textSize = 16f
                textStyle = bold
                text = "更新数据"
                gravity = gravity_center
                layout_id = "tv_permission"
                onClick = {
                    LiveEventBus.get(DemoEvent::class.java).post(DemoEvent("ssr"))

                }
                top_toTopOf = parent_id
                start_toStartOf = parent_id
                background_color = "#eeeeee"

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weakReference = WeakReference(this)
        setContentView(rootView)



    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}