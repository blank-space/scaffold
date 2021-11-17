package com.dawn.launch.app

import android.app.Application
import android.util.Log
import com.dawn.base.BaseAppInit
import com.dawn.base.log.L
import com.google.auto.service.AutoService

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/3
 * @desc   :
 */
@AutoService(BaseAppInit::class)
class LaunchModuleInit : BaseAppInit {
    override fun onInitSpeed(app: Application) {
        Log.d("@@","LaunchModuleInit#onInitSpeed")
    }

    override fun onInitLow(app: Application) {

        Log.d("@@","LaunchModuleInit#onInitLow")


    }
}