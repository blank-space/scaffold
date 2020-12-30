package com.bsnl.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.base.log.L
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.ViewHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.feature_sample_pkg_activity_async.*
import javax.inject.Inject

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/24
 * @desc   :
 */
@AndroidEntryPoint
class AsyncCreateView2Activity : AppCompatActivity() {
    companion object {
        fun startAction(context: Context) {
            startActivity<AsyncCreateView2Activity>(context)
        }
    }

    @Inject
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