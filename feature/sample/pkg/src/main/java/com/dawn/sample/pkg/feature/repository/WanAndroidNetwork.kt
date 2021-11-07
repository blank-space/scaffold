package com.dawn.sample.pkg.feature.repository

import com.dawn.base.DataResult
import com.dawn.network.ServiceCreator
import com.dawn.sample.pkg.feature.apiService.WanAndroidService
import com.dawn.sample.pkg.feature.data.entity.UserInfo
import okhttp3.RequestBody
import retrofit2.http.Body

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
object WanAndroidNetwork {

    private val service = com.dawn.network.ServiceCreator.create<WanAndroidService>()

    suspend fun getTopArticles() = service.getTopArticles()

    suspend fun getBanners() = service.getBanners()

    suspend fun login() = service.login("lizhaoxing","123456")


}