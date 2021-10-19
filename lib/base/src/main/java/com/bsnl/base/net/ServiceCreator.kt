package com.bsnl.base.net

import android.os.Build
import com.bsnl.base.BuildConfig
import com.bsnl.base.net.interceptors.RetryInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.paradisehell.convex.converter.ConvexConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/5/14
 * @desc   : Retrofit封装类，获取Service
 */
object ServiceCreator {
    //var BASE_URL = "https://pokeapi.co/api/v2/"
    var BASE_URL ="https://www.wanandroid.com/"
    private const val TIME_OUT_SECONDS = 20

    //不同的项目或许需要不同的拦截器，自行扩展即可
    val interceptors = mutableListOf<Interceptor>()

    private var retrofit = initRetrofit()

    /**
     * 重新初始化Retrofit
     */
    fun initRetrofit(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * @sample  ServiceCreator.create<WanAndroidService>()
     **/
    inline fun <reified T> create(): T = create(T::class.java)

    fun getClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
            .addInterceptor(RetryInterceptor())
            .addInterceptor(getLogInterceptor())

        if (interceptors.isNotEmpty()) {
            for (index in 0 until interceptors.size) {
                builder.addInterceptor(interceptors[index])
            }
        }
        return builder.build()
    }



    private fun getLogInterceptor(): Interceptor {
        if (!BuildConfig.LOG_DEBUG) {
            //Release 时, 让框架不再打印 Http 请求和响应的信息
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        } else {
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }



}