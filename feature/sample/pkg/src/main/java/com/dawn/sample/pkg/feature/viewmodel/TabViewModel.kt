package com.dawn.sample.pkg.feature.viewmodel

import com.dawn.base.DataResult
import com.dawn.base.viewmodel.base.BaseListViewModel
import com.dawn.sample.pkg.feature.data.model.PokemonListResponse
import com.dawn.sample.pkg.feature.repository.impl.PokeRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   :
 */
class TabViewModel : BaseListViewModel() {
    val repository by lazy {
        PokeRepositoryImpl
    }

    override fun getList(): Flow<DataResult<out Any>?>? {
        return flow {
            val response = repository.fetchPokemonList(pageNo)
            val data = DataResult<PokemonListResponse>()
            response.collectLatest {
                data.data = it
                data.code ="000000"
                data.msg = "ok"
            }
            emit(data)
        }
    }
}