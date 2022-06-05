package com.dawn.sample.pkg.feature.apiService

import com.dawn.base.DataResult
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.data.entity.*
import retrofit2.http.*

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
interface WanAndroidService {
    // 置顶
    @GET("/article/top/json")
    suspend fun getTopArticles(): WanResult<List<Article>>

    // 首页
    @GET("/article/list/{page}/json")
    suspend fun getIndexList(@Path("page") page: Int): WanResult<ListWrapper<Article>>

    @GET("/banner/json")
    suspend fun getBanners(): DataResult<List<Banner>>

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResult<UserInfo>

}