package com.bsnl.sample.pkg.feature.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import com.bsnl.common.BaseHttpResult
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.sample.pkg.feature.data.model.PokeItemModel
import com.bsnl.sample.pkg.feature.data.model.PokemonListResponse
import com.bsnl.sample.pkg.feature.repository.IPokeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   :
 */
class TabViewModel @ViewModelInject constructor(
    val repository: IPokeRepository
):BaseListViewModel() {

    override fun getList(): Flow<BaseHttpResult<Any>?>? {
        return flow {
            val response = repository.fetchPokemonList(pageNo)
            val data = BaseHttpResult<PokemonListResponse>()
            response.collectLatest {
                data.data = it
                data.code = 0
                data.msg = "ok"
            }
            emit(data)
        } as Flow<BaseHttpResult<Any>?>?
    }
}