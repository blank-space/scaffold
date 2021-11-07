package com.dawn.sample.pkg.feature.apiService

import com.dawn.base.DataResult
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.data.entity.Banner
import com.dawn.sample.pkg.feature.data.entity.UserInfo
import com.dawn.sample.pkg.feature.data.entity.WanResult
import okhttp3.RequestBody
import retrofit2.http.*

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

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResult<UserInfo>

}