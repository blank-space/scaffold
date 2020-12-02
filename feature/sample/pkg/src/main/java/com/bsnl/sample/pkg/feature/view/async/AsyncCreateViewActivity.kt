package com.bsnl.sample.pkg.feature.view.async

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.base.log.L
import com.bsnl.common.page.base.BaseActivity
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.replaceLooperWithMainThreadQueue
import com.bsnl.common.utils.startActivity
import com.bsnl.common.viewmodel.StubViewModel
import com.bsnl.sample.pkg.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        Thread{
            if (replaceLooperWithMainThreadQueue(false)) {
                setContentView(R.layout.feature_sample_pkg_activity_async)
                replaceLooperWithMainThreadQueue(true)
            }
        }.start()
    }


}