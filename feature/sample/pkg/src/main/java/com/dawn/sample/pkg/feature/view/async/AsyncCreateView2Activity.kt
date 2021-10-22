package com.dawn.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dawn.base.log.L
import com.dawn.base.utils.startActivity
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.feature.view.async.asyncLoadView.ViewHelper

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/24
 * @desc   :
 */
class AsyncCreateView2Activity : AppCompatActivity() {
    companion object {
        fun startAction(context: Context) {
            startActivity<AsyncCreateView2Activity>(context)
        }
    }

    private val root_main:ViewGroup by lazy {
        findViewById(R.id.root_main)
    }


    lateinit var viewHelper: ViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_sample_pkg_activity_async)
        //sample code
        val view = viewHelper.getView(R.layout.feature_sample_pkg_fragment_first)
        view?.let {
            viewHelper.refreshCurrentActivity(this)
            if (it.parent != null) {
                val prt = it.parent as ViewGroup
                L.e("call parent remove")
                prt.removeView(it)
            }
            root_main.addView(it)
        }

    }

    override fun onDestroy() {
        root_main.removeAllViews()
        viewHelper.recycleView(R.layout.feature_sample_pkg_fragment_first)
        super.onDestroy()
    }
}