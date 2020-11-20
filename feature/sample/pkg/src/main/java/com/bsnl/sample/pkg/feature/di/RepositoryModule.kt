package com.bsnl.sample.pkg.feature.di

import com.bsnl.sample.pkg.feature.apiService.PokemonService
import com.bsnl.sample.pkg.feature.data.dao.AppDataBase
import com.bsnl.sample.pkg.feature.repository.IPokeRepository
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
import com.bsnl.sample.pkg.feature.repository.RepositoryFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideArticleRepository(
        api: PokemonNetwork,
        db: AppDataBase
    ): IPokeRepository {
        return RepositoryFactory.createArticleRepository(api, db)
    }
}