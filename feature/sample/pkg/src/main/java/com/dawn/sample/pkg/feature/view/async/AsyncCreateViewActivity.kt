package com.dawn.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dawn.base.utils.startActivity
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.feature.view.async.asyncLoadView.ViewHelper

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/24
 * @desc   :
 */
class AsyncCreateViewActivity : AppCompatActivity() {
    companion object {
        fun startAction(context: Context) {
            startActivity<AsyncCreateViewActivity>(context)
        }
    }
    private val root_main: ViewGroup by lazy {
        findViewById(R.id.root_main)
    }
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