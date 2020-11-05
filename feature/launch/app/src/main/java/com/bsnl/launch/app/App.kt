package com.bsnl.launch.app

import android.os.Looper
import android.util.Log
import com.bsnl.base.BaseApp
import com.bsnl.base.log.L
import com.bsnl.base.net.ServiceCreator
import com.bsnl.base.utils.GlobalHandler
import com.bsnl.common.ui.viewStatus.Gloading
import com.bsnl.common.ui.viewStatus.adapter.GlobalAdapter
import com.bsnl.launch.app.webview.WebViewPool

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        ServiceCreator.BASE_URL = "https://pokeapi.co/api/v2/"
        ServiceCreator.initRetrofit()
        Gloading.debug(BuildConfig.LOG_DEBUG)
        Gloading.initDefault(GlobalAdapter())
        WebViewPool.init()
       // loop()
    }


    /**
     * 接管Loop,处理一些系统级的没必要的闪退：当他们发生时进行try-catch并不会引起上下文混乱
     */
    fun loop() {
        GlobalHandler.post(
            Runnable {
                while (true) {
                    try {
                        Looper.loop()
                    } catch (e: Throwable) {
                        val stack = Log.getStackTraceString(e)
                        if (stack.contains("Toast")
                            || stack.contains("SelectionHandleView")) {
                            L.e("捕获到异常，在线程${Thread.currentThread().name}, 在${e.stackTrace[0].className}")
                        } else {
                            //不可接受的异常，让他crash
                            throw  e
                        }
                    }
                }
            }
        )
    }
}