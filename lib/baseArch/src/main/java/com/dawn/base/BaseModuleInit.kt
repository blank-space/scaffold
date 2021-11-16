package com.dawn.base

import android.app.Application
import android.util.Log
import com.google.auto.service.AutoService

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/10
 * @desc   :
 */
@AutoService(BaseAppInit::class)
class BaseModuleInit : BaseAppInit {
    override fun onInitSpeed(app: Application) {
        Log.d("@@","BaseModuleInit#onInitSpeed")
    }

    override fun onInitLow(app: Application) {
        Log.d("@@","BaseModuleInit#onInitSpeed")
    }
}