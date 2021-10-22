package com.dawn.sample.pkg.feature.di

import com.dawn.base.net.ServiceCreator
import com.dawn.sample.pkg.feature.repository.PokemonNetwork
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/6
 */

object NetWorkModule {


    fun provideOkHttpClient(): OkHttpClient {
        return ServiceCreator.getClient()
    }



    fun provideRetrofit(): Retrofit {
        return ServiceCreator.initRetrofit()
    }


    fun providePokemonNetwork(): PokemonNetwork {
        return PokemonNetwork
    }


}