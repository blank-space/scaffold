package com.dawn.sample.pkg.feature.repository.impl

import android.util.Log
import com.dawn.base.BaseListBean
import com.dawn.base.DataResult
import com.dawn.base.log.L
import com.dawn.base.utils.simpleSlow
import com.dawn.sample.pkg.feature.data.entity.*
import com.dawn.sample.pkg.feature.repository.IWanAndroidRepository
import com.dawn.sample.pkg.feature.repository.WanAndroidNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import okhttp3.RequestBody
import java.util.*

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class WanAndroidRepository : IWanAndroidRepository {
    private val network by lazy { WanAndroidNetwork }

    @ExperimentalCoroutinesApi
    override fun getTopArticles(): Flow<DataResult<List<Article>>> {
        return simpleSlow {
            L.e("getTopArticles from remote source")
            emit(network.getTopArticles())
        }
    }

    @ExperimentalCoroutinesApi
    override fun getBanners(): Flow<DataResult<List<Banner>>> {
        return simpleSlow { emit(network.getBanners()) }
    }

    override fun downLoadFile(downloadFile: DownloadFile, result: DownloadFile.DownLoadResult) {
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

    override fun login(): Flow<WanResult<UserInfo>> {
        return simpleSlow {
            emit(network.login())
        }
    }

}