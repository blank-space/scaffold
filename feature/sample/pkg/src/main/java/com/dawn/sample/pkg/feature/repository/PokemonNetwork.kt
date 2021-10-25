package com.dawn.sample.pkg.feature.repository

import com.dawn.network.ServiceCreator
import com.dawn.sample.pkg.feature.apiService.PokemonService

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
object PokemonNetwork {
    private val service = com.dawn.network.ServiceCreator.create<PokemonService>()

    suspend fun fetchPokemonList(pageNo: Int) = service.fetchPokemonList(offset = pageNo*20)

}