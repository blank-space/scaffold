package com.dawn.sample.pkg.feature.repository

import com.dawn.base.BaseHttpResult
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.data.entity.Banner
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
interface IWanAndroidRepository {

    fun getTopArticles(): Flow<BaseHttpResult<List<Article>>>

    fun getBanners(): Flow<BaseHttpResult<List<Banner>>>

}