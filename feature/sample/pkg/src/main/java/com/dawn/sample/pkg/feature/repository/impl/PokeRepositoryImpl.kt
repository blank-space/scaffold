package com.dawn.sample.pkg.feature.repository.impl


import com.dawn.sample.pkg.feature.apiService.PokemonService
import com.dawn.sample.pkg.feature.data.model.PokemonListResponse
import com.halvie.network.base.BaseNetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
object PokeRepositoryImpl: BaseNetworkApi<PokemonService>() {
    var localPage = 1

    /**
     * 尝试做数据转换
     */
     fun fetchPokemonList(pageNo: Int): Flow<PokemonListResponse> {
        if (pageNo == 0) {
            localPage = 1
        }
        return flow {
            val response: PokemonListResponse?
            response = service.fetchPokemonList(offset = pageNo*20)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }

     fun mockSearch(): Flow<String> {
        return flow {
            emit("mock search。。。")
        }.flowOn(Dispatchers.IO)
    }


}