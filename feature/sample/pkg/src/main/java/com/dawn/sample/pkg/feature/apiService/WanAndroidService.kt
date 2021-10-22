package com.dawn.sample.pkg.feature.apiService

import com.dawn.base.BaseHttpResult
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
    suspend fun getTopArticles(): BaseHttpResult<List<Article>>

    @GET("/banner/json")
    suspend fun getBanners(): BaseHttpResult<List<Banner>>

}