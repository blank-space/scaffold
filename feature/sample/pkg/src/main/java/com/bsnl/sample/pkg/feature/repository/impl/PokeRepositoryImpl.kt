package com.bsnl.sample.pkg.feature.repository.impl


import com.bsnl.base.log.L
import com.bsnl.common.constant.DEFAULT_PAGE_SIZE
import com.bsnl.sample.pkg.feature.data.entity.PokeItemEntity
import com.bsnl.sample.pkg.feature.data.model.PokemonListResponse
import com.bsnl.sample.pkg.feature.data.model.PokeItemModel
import com.bsnl.sample.pkg.feature.mapper.Mapper
import com.bsnl.sample.pkg.feature.repository.IPokeRepository
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
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
            var response: PokemonListResponse?
            response = network.fetchPokemonList(pageNo)
            emit(response)
        }.flowOn(Dispatchers.IO)
    }


}