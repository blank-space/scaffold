package com.dawn.sample.pkg.feature.repository

import com.dawn.base.DataResult
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.data.entity.Banner
import com.dawn.sample.pkg.feature.data.entity.DownloadFile
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
interface IWanAndroidRepository {

    fun getTopArticles(): Flow<DataResult<List<Article>>>

    fun getBanners(): Flow<DataResult<List<Banner>>>

    fun downLoadFile(downloadFile: DownloadFile,result: DownloadFile.DownLoadResult)
}