package com.bsnl.sample.pkg.feature.di

import android.app.Application
import androidx.room.Room
import com.bsnl.sample.pkg.feature.data.dao.AppDataBase
import com.bsnl.sample.pkg.feature.data.dao.PokeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :  这里使用了 ApplicationComponent，因此 NetworkModule 绑定到 Application 的生命周期。
 */
@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDataBase(application: Application): AppDataBase {
        return Room
            .databaseBuilder(application, AppDataBase::class.java, "sample.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()//允许在主线程中查询
            .build()
    }

    @Provides
    @Singleton
    fun providerPokemonDao(appDataBase: AppDataBase): PokeDao {
        return appDataBase.pokeDao()
    }





}