package com.dawn.sample.pkg.feature.repository

import com.dawn.sample.pkg.feature.data.model.PokemonListResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
interface IPokeRepository {

    fun fetchPokemonList(pageNo: Int): Flow<PokemonListResponse>

}