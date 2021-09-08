package com.bsnl.sample.pkg.feature.viewmodel

import com.bsnl.common.BaseHttpResult
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.sample.pkg.feature.data.model.PokemonListResponse
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
import com.bsnl.sample.pkg.feature.repository.impl.PokeRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   :
 */
class TabViewModel :BaseListViewModel() {
    val repository by lazy {
        PokeRepositoryImpl(PokemonNetwork)
    }

    override fun getList(): Flow<BaseHttpResult<Any>?>? {
        return flow {
            val response = repository.fetchPokemonList(pageNo)
            val data = BaseHttpResult<PokemonListResponse>()
            response.collectLatest {
                data.data = it
                data.code ="000000"
                data.msg = "ok"
            }
            emit(data)
        } as Flow<BaseHttpResult<Any>?>?
    }
}