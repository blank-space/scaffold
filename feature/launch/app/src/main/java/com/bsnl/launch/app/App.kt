package com.bsnl.launch.app

import com.bsnl.base.BaseApp
import com.bsnl.base.net.ServiceCreator
import com.bsnl.common.ui.viewStatus.Gloading
import com.bsnl.common.ui.viewStatus.adapter.GlobalAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        ServiceCreator.BASE_URL="https://pokeapi.co/api/v2/"
        ServiceCreator.initRetrofit()
        Gloading.debug(BuildConfig.LOG_DEBUG)
        Gloading.initDefault(GlobalAdapter())

    }
}