package com.bsnl.sample.pkg.feature.di

import com.bsnl.base.net.ServiceCreator
import com.bsnl.sample.pkg.feature.apiService.PokemonService
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/6
 * @desc   : 这里使用了 ApplicationComponent，因此 NetworkModule 绑定到 Application 的生命周期。
 */
@Module
@InstallIn(ApplicationComponent::class)
object NetWorkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return ServiceCreator.getClient()
    }


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return ServiceCreator.initRetrofit()
    }

    @Provides
    @Singleton
    fun providePokemonNetwork(): PokemonNetwork {
        return PokemonNetwork
    }
}