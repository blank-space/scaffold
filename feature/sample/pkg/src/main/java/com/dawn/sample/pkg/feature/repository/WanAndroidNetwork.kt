package com.dawn.sample.pkg.feature.repository

import com.dawn.network.ServiceCreator
import com.dawn.sample.pkg.feature.apiService.WanAndroidService

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
object WanAndroidNetwork {

    private val service = com.dawn.network.ServiceCreator.create<WanAndroidService>()

    suspend fun getTopArticles() = service.getTopArticles()

    suspend fun getBanners() = service.getBanners()

}