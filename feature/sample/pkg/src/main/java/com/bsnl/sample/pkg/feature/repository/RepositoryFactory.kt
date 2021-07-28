package com.bsnl.sample.pkg.feature.repository

import com.bsnl.sample.pkg.feature.repository.impl.PokeRepositoryImpl

object RepositoryFactory {

    fun createArticleRepository(api: PokemonNetwork): IPokeRepository = PokeRepositoryImpl(api)

}
