package com.dawn.sample.pkg.feature.apiService

import com.dawn.base.DataResult
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.data.entity.Banner
import retrofit2.http.GET

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
interface WanAndroidService {

    @GET("/article/top/json")
    suspend fun getTopArticles(): DataResult<List<Article>>

    @GET("/banner/json")
    suspend fun getBanners(): DataResult<List<Banner>>

}