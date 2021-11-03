package com.dawn.sample.pkg

import android.app.Application
import android.util.Log
import com.dawn.base.BaseAppInit
import com.dawn.base.log.L
import com.google.auto.service.AutoService

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/19
 * @desc   :
 */
@AutoService(BaseAppInit::class)
class SampleModuleInit : BaseAppInit {

    override fun onInitSpeed(app: Application) {
        Log.d("@@","SampleInit#onInitSpeed")
    }

    override fun onInitLow(app: Application) {
        Log.d("@@","SampleInit#onInitLow")
    }
}