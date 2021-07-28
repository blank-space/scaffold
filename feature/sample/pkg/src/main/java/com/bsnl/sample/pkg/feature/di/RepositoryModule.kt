package com.bsnl.sample.pkg.feature.di

import com.bsnl.sample.pkg.feature.repository.IPokeRepository
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
import com.bsnl.sample.pkg.feature.repository.RepositoryFactory

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
class RepositoryModule {

    fun provideArticleRepository(
        api: PokemonNetwork): IPokeRepository {
        return RepositoryFactory.createArticleRepository(api)
    }
}