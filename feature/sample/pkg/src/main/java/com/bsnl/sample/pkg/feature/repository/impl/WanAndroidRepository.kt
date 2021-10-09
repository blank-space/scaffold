package com.bsnl.sample.pkg.feature.repository.impl

import com.bsnl.common.BaseHttpResult
import com.bsnl.sample.pkg.feature.data.entity.Article
import com.bsnl.sample.pkg.feature.data.entity.Banner
import com.bsnl.sample.pkg.feature.repository.IWanAndroidRepository
import com.bsnl.sample.pkg.feature.repository.WanAndroidNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class WanAndroidRepository : IWanAndroidRepository {
    private val network by lazy { WanAndroidNetwork }

    override fun getTopArticles(): Flow<BaseHttpResult<List<Article>>>{
        return flow {
            emit(network.getTopArticles())
        }.flowOn(Dispatchers.IO)
    }

    override fun getBanners(): Flow<BaseHttpResult<List<Banner>>> {
        return flow {
            emit(network.getBanners())
        }.flowOn(Dispatchers.IO)
    }


}