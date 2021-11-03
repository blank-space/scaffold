package com.dawn.base

import android.app.Application
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
        mLoader.forEach {
            it.onInitSpeed(app)
        }
    }

    override fun onInitLow(app: Application) {
        mLoader.forEach {
            it.onInitLow(app)
        }
    }
}