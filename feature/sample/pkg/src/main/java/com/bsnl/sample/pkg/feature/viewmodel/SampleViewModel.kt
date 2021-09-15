package com.bsnl.sample.pkg.feature.viewmodel

import com.bsnl.common.BaseHttpResult
import com.bsnl.common.BaseListBean
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.sample.pkg.feature.apiService.WanAndroidService
import com.bsnl.sample.pkg.feature.data.entity.Article
import com.bsnl.sample.pkg.feature.data.model.PokemonListResponse
import com.bsnl.sample.pkg.feature.repository.IPokeRepository
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
import com.bsnl.sample.pkg.feature.repository.impl.PokeRepositoryImpl
import com.bsnl.sample.pkg.feature.repository.impl.WanAndroidRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class SampleViewModel : BaseListViewModel() {

    private val repository by lazy {
        WanAndroidRepository()
    }

    override fun getList(): Flow<BaseHttpResult<Any>?>? {
        return flow {
            val response = repository.getTopArticles()
            val data = BaseHttpResult<BaseListBean<Article>>()
            response.collectLatest {
                data.data = it
                data.code = "000000"
                data.msg = "ok"
            }
            emit(data)
        } as Flow<BaseHttpResult<Any>?>?
    }
}