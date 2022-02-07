package com.halvie.network.base

import com.alibaba.network.BuildConfig
import com.halvie.network.adapter.BigDecimalAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import de.stefanmedack.adapter.DefaultOnDataMismatchAdapter
import de.stefanmedack.adapter.FilterNullValuesFromListAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.ParameterizedType
import java.util.*
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 20L

abstract class BaseNetworkApi<I>(private val baseUrl: String = "https://www.wanandroid.com/") :
    IService<I> {
    val moshi = Moshi.Builder().add(BigDecimalAdapter)
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(DefaultOnDataMismatchAdapter.newFactory(Any::class.java, null))
        .add(FilterNullValuesFromListAdapter.newFactory(Any::class.java))
        .build()

    protected val service: I by lazy {
        getRetrofit().create(getServiceClass())
    }

    open fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun getServiceClass(): Class<I> {
        val genType = javaClass.genericSuperclass as ParameterizedType
        return genType.actualTypeArguments[0] as Class<I>
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClient = getCustomOkHttpClient()
        if (null != okHttpClient) {
            return okHttpClient
        }
        return defaultOkHttpClient
    }

    open fun getCustomOkHttpClient(): OkHttpClient {
        return defaultOkHttpClient
    }

    protected open fun getCustomInterceptor(): Interceptor? {
        return null
    }


    companion object {
        const val TAG = "BaseNetworkApi"
        private const val RETRY_COUNT = 2
        private val defaultOkHttpClient by lazy {
            val builder = OkHttpClient.Builder()
                .callTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)


            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }

            builder.build()
        }
    }
}

