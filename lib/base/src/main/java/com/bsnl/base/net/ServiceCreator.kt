package com.bsnl.base.net

import android.os.Build
import com.bsnl.base.BuildConfig
import com.bsnl.base.net.interceptors.RetryInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.jvm.Throws

/**
 * @author : LeeZhaoXing
 * @date   : 2020/5/14
 * @desc   : Retrofit封装类，获取Service
 */
object ServiceCreator {
    var BASE_URL = "https://pokeapi.co/api/v2/"
    private const val TIME_OUT_SECONDS = 20

    //不同的项目或许需要不同的拦截器，自行扩展即可
    val interceptors = mutableListOf<Interceptor>()

    private var retrofit = initRetrofit()


    /**
     * 重新初始化Retrofit
     */
    fun initRetrofit():Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit
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
            //.addInterceptor(RetryInterceptor())
            .addInterceptor(getLogInterceptor())

        if (interceptors.isNotEmpty()) {
            for (index in 0..interceptors.size - 1) {
                builder.addInterceptor(interceptors[index])
            }
        }
        if (Build.VERSION.SDK_INT < 29) {
            createSSLSocketFactory()?.let {
                builder.sslSocketFactory(it)
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


    /**
     * 实现https请求
     */
    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (e: Exception) {
        }
        return ssfFactory
    }

    private class TrustAllCerts : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
        }


        override fun getAcceptedIssuers(): Array<X509Certificate?>? {
            return arrayOfNulls(0)
        }
    }

    /**
     * 信任所有的服务器,返回true
     */
    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}