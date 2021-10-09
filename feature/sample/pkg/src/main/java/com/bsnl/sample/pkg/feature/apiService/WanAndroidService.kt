package com.bsnl.sample.pkg.feature.apiService

import com.bsnl.common.BaseHttpResult
import com.bsnl.sample.pkg.feature.data.entity.Article
import com.bsnl.sample.pkg.feature.data.entity.Banner
import com.bsnl.sample.pkg.feature.transformer.WanAndroidConvexTransformer
import kotlinx.coroutines.flow.Flow
import org.paradisehell.convex.annotation.DisableTransformer
import org.paradisehell.convex.annotation.Transformer
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



    /*@GET("/users")
    @DisableTransformer // Convex will ignore UserConvexTransformer
    suspend fun getAllUsers() : BaseResponse<List<User>>*/
}