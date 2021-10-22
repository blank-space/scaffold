package com.dawn.sample.pkg

import android.app.Application
import com.dawn.base.BaseAppInit
import com.dawn.base.log.L

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/19
 * @desc   :
 */
class SampleInit : BaseAppInit {

    override fun onInitSpeed(app: Application): Boolean {
        L.d("SampleInit#onInitSpeed")
        return true
    }

    override fun onInitLow(app: Application): Boolean {
        L.d("SampleInit#onInitLow")
        return true
    }
}