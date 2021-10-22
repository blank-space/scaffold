package com.dawn.base.net.interceptors

import com.dawn.base.log.L
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/12
 * @desc   : 错误重试
 */
class RetryInterceptor(val maxRetry: Int = 3) : Interceptor {

    private var retryNum = 0 //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        println(">>>>>>> retryNum=$retryNum")
        var response = chain.proceed(request)
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum++
            L.e( "retryNum=$retryNum")
            response = chain.proceed(request)
        }
        retryNum = 0
        return response
    }


}