package com.dawn.sample.pkg.feature.viewmodel

import com.dawn.base.BaseHttpResult
import com.dawn.base.BaseListBean
import com.dawn.base.viewmodel.base.BaseListViewModel
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository
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
                data.data = BaseListBean(it.data)
                data.code = "000000"
                data.msg = "ok"
            }
            emit(data)
        } as Flow<BaseHttpResult<Any>?>?
    }
}