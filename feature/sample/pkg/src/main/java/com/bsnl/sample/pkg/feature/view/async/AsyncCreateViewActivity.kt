package com.bsnl.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.base.BaseApp
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
class AsyncCreateViewActivity : AppCompatActivity() {
    companion object {
        fun startAction(context: Context) {
            startActivity<AsyncCreateViewActivity>(context)
        }
    }

    @Inject
    lateinit var viewHelper: ViewHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_sample_pkg_activity_async)
        viewHelper.getViewContext()?.let {
            if(it.getCurrentContext()==null){
                viewHelper.refreshCurrentActivity(this)
            }
        }
        viewHelper.asyncPreLoadView(R.layout.feature_sample_pkg_fragment_first)
        val btn = Button(this)
        btn.setText("jump")
        btn.setOnClickListener {
            AsyncCreateView2Activity.startAction(this@AsyncCreateViewActivity)
        }
        root_main.addView(btn)
    }

}