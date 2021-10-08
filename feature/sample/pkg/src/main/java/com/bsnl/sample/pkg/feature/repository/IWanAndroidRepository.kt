package com.bsnl.sample.pkg.feature.repository

import com.bsnl.common.BaseListBean
import com.bsnl.sample.pkg.feature.data.entity.Article
import com.bsnl.sample.pkg.feature.data.entity.Banner
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
interface IWanAndroidRepository {

    fun getTopArticles(): Flow<BaseListBean<Article>>

    fun getBanners(): Flow<BaseListBean<Banner>>
}