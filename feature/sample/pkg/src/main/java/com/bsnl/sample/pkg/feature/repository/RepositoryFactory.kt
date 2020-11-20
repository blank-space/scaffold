package com.bsnl.sample.pkg.feature.repository

import com.bsnl.sample.pkg.feature.apiService.PokemonService
import com.bsnl.sample.pkg.feature.data.dao.AppDataBase
import com.bsnl.sample.pkg.feature.mapper.PokemonMapper
import com.bsnl.sample.pkg.feature.repository.impl.PokeRepositoryImpl

object RepositoryFactory {

    fun createArticleRepository(api: PokemonNetwork, db: AppDataBase): IPokeRepository = PokeRepositoryImpl(api, db)

}
