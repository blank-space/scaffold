package com.bsnl.sample.pkg.feature.repository

import com.bsnl.common.BaseHttpResult
import com.bsnl.sample.pkg.feature.model.ListingData
import com.bsnl.sample.pkg.feature.model.ListingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
object SampleRepository {
    val network by lazy { SampleNetwork }

    fun fetchPokemonList(pageNo: Int): Flow<ListingResponse> {
        return flow {
            val model = network.fetchPokemonList(pageNo)
            emit(model)
        }.flowOn(Dispatchers.IO)
    }
}