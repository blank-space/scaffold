package com.dawn.base

import android.app.Application
import android.util.Log
import com.dawn.base.utils.doOnMainThreadIdle
import java.util.*

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/3
 * @desc   : 组件初始化的工作将由该代理类代理实现
 */
class LoadModuleProxy : BaseAppInit {

    private var mLoader: ServiceLoader<BaseAppInit> =
        ServiceLoader.load(BaseAppInit::class.java)

    override fun onInitSpeed(app: Application) {
        var count = 0
        mLoader.forEach {
            it.onInitSpeed(app)
            count++
        }
        Log.d("@@","loader count:$count")
    }

    override fun onInitLow(app: Application) {
        doOnMainThreadIdle({
            mLoader.forEach {
                it.onInitLow(app)
            }
        },0)
    }
}