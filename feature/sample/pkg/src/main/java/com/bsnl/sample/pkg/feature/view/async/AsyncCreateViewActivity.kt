package com.bsnl.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pools
import androidx.core.view.postDelayed
import com.bsnl.base.utils.showToast
import com.bsnl.common.utils.replaceLooperWithMainThreadQueue
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.view.async.pool.AsyncLayoutUtil
import kotlinx.android.synthetic.main.feature_sample_pkg_activity_async.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_sample_pkg_activity_async)
        GlobalScope.launch(Dispatchers.IO){
            if (replaceLooperWithMainThreadQueue(false)) {
                val textView = TextView(this@AsyncCreateViewActivity)
                val btn =Button(this@AsyncCreateViewActivity)

                replaceLooperWithMainThreadQueue(true)
                textView.setText("textView")
                btn.setText("jump")
                btn.setOnClickListener {
                    AsyncCreateView2Activity.startAction(this@AsyncCreateViewActivity)
                    finish()
                }
                textView.postDelayed(1000){
                    textView.setText("it's textView")

                }
                AsyncLayoutUtil.mRequestPool.release(textView)
                AsyncLayoutUtil.mRequestPool.release(btn)


                runOnUiThread {
                    root_main.addView(textView)
                    root_main.addView(btn)
                }



            }
        }

    }


}