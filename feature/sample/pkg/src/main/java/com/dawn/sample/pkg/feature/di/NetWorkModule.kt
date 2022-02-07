package com.dawn.sample.pkg.feature.di

import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository
import com.halvie.network.base.BaseNetworkApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/6
 */

object NetWorkModule {


    fun provideOkHttpClient(): OkHttpClient {
        return WanAndroidRepository.getCustomOkHttpClient()
    }



    fun provideRetrofit(): Retrofit {
        return WanAndroidRepository.getRetrofit()
    }

}