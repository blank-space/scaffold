package com.bsnl.sample.pkg.feature.repository

import com.bsnl.base.net.ServiceCreator
import com.bsnl.sample.pkg.feature.apiService.PokemonService

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
object SampleNetwork {
    private val service = ServiceCreator.create<PokemonService>()

    suspend fun fetchPokemonList(pageNo: Int) = service.fetchPokemonList(offset = pageNo)

}