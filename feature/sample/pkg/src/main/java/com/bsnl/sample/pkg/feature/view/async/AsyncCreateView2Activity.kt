package com.bsnl.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pools
import com.bsnl.base.log.L
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.view.async.pool.AsyncLayoutUtil
import kotlinx.android.synthetic.main.feature_sample_pkg_activity_async.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_sample_pkg_activity_async)

        //sample code
        for (index in AsyncLayoutUtil.mRequestPool.mPool.indices) {
            val view = AsyncLayoutUtil.mRequestPool.acquire()
            view?.let {
                if (it.parent != null) {
                    val prt = it.parent as ViewGroup
                    prt.removeView(it)
                }
                root_main.addView(it)
            }

        }


    }


}