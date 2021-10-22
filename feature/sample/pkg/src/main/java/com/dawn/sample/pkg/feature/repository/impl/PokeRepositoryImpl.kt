package com.dawn.sample.pkg.feature.repository.impl


import com.dawn.sample.pkg.feature.data.model.PokemonListResponse
import com.dawn.sample.pkg.feature.repository.IPokeRepository
import com.dawn.sample.pkg.feature.repository.PokemonNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
class PokeRepositoryImpl(
    val network: PokemonNetwork,
    ) : IPokeRepository {
    var localPage = 1

    /**
     * 尝试做数据转换
     */
    override fun fetchPokemonList(pageNo: Int): Flow<PokemonListResponse> {
        if (pageNo == 0) {
            localPage = 1
        }
        return flow {
            val response: PokemonListResponse?
            response = network.fetchPokemonList(pageNo)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }


}