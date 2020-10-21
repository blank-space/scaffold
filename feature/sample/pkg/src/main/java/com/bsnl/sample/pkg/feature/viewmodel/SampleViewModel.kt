package com.bsnl.sample.pkg.feature.viewmodel

import com.bsnl.common.BaseHttpResult
import com.bsnl.common.utils.fetchLiveData
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.sample.pkg.feature.model.ListingData
import com.bsnl.sample.pkg.feature.model.ListingResponse
import com.bsnl.sample.pkg.feature.repository.SampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class SampleViewModel : BaseListViewModel() {

    override fun getList(): Flow<BaseHttpResult<Any>?>? {
        return flow {
            val response = SampleRepository.fetchPokemonList(pageNo)
            val data = BaseHttpResult<ListingResponse>()
            response.collectLatest {
                data.data = it
                data.code = 0
                data.msg = "ok"
            }
            emit(data)
        } as Flow<BaseHttpResult<Any>?>?
    }
}