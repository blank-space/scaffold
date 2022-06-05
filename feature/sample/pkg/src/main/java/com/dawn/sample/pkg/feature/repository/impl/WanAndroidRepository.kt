package com.dawn.sample.pkg.feature.repository.impl

import com.dawn.base.DataResult
import com.dawn.base.utils.simpleSlow
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.apiService.WanAndroidService
import com.dawn.sample.pkg.feature.data.entity.*
import com.halvie.network.base.BaseNetworkApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
object WanAndroidRepository : BaseNetworkApi<WanAndroidService>() {

    @ExperimentalCoroutinesApi
    fun getTopArticles(): Flow<WanResult<List<Article>>> {
        return simpleSlow {
            emit(service.getTopArticles())
        }
    }


    @ExperimentalCoroutinesApi
    fun getIndexList(index: Int = 0): Flow<WanResult<ListWrapper<Article>>> {
        return simpleSlow {
            emit(service.getIndexList(index))
        }
    }

    @ExperimentalCoroutinesApi
    fun getBanners(): Flow<DataResult<List<Banner>>> {
        return simpleSlow { emit(service.getBanners()) }
    }

    fun downLoadFile(downloadFile: DownloadFile, result: DownloadFile.DownLoadResult) {
        val timer = Timer()
        val dataResult = DataResult<DownloadFile>().apply {
            code = "000000"
            data = downloadFile
        }
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层
                if (downloadFile.process < 100) {
                    downloadFile.process += 1
                    //.d("@@", "下载进度 " + downloadFile.process.toString() + "%")
                } else {
                    timer.cancel()
                }
                if (downloadFile.forgive) {
                    timer.cancel()
                    downloadFile.process = 0
                    downloadFile.forgive = false
                    return
                }
                result.onResult(dataResult)
            }
        }

        timer.schedule(task, 100, 100)
        val baseHttpResult = DataResult<Any>().apply {
            code = "000000"
            data = Any()
        }

    }

    fun login(): Flow<WanResult<UserInfo>> {
        return simpleSlow {
            emit(service.login("lizhaoxing", "123456"))
        }
    }

}