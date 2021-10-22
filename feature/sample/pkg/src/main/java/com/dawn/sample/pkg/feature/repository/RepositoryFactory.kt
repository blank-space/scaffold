package com.dawn.sample.pkg.feature.repository

import com.dawn.sample.pkg.feature.repository.impl.PokeRepositoryImpl

object RepositoryFactory {

    fun createArticleRepository(api: PokemonNetwork): IPokeRepository = PokeRepositoryImpl(api)

}
