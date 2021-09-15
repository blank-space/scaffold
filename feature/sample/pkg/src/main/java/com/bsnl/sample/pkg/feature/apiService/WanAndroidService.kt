package com.bsnl.sample.pkg.feature.apiService

import com.bsnl.sample.pkg.feature.data.entity.Article
import com.bsnl.sample.pkg.feature.data.entity.Banner
import com.bsnl.sample.pkg.feature.transformer.WanAndroidConvexTransformer
import org.paradisehell.convex.annotation.Transformer
import retrofit2.http.GET

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
@Transformer(WanAndroidConvexTransformer::class)
interface WanAndroidService {

    @GET("/article/top/json")
    suspend fun getTopArticles(): List<Article>

    @GET("/banner/json")
    suspend fun getBanners(): List<Banner>

    /*@GET("/users")
    @DisableTransformer // Convex will ignore UserConvexTransformer
    suspend fun getAllUsers() : BaseResponse<List<User>>*/
}