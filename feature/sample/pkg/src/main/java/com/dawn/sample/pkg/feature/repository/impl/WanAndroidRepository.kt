package com.dawn.sample.pkg.feature.repository.impl

import com.dawn.base.BaseHttpResult
import com.dawn.base.utils.simpleSlow
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.data.entity.Banner
import com.dawn.sample.pkg.feature.repository.IWanAndroidRepository
import com.dawn.sample.pkg.feature.repository.WanAndroidNetwork
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class WanAndroidRepository : IWanAndroidRepository {
    private val network by lazy { WanAndroidNetwork }

    override fun getTopArticles(): Flow<BaseHttpResult<List<Article>>>{
        return simpleSlow { emit(network.getTopArticles()) }
    }

    override fun getBanners(): Flow<BaseHttpResult<List<Banner>>> {
        return simpleSlow { emit(network.getBanners()) }
    }

}