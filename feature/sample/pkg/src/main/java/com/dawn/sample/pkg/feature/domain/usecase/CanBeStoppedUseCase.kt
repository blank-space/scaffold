package com.dawn.sample.pkg.feature.domain.usecase

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.dawn.base.DataResult
import com.dawn.domain.usecase.UseCase
import com.dawn.sample.pkg.feature.data.entity.DownloadFile
import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/26
 * @desc   :
 */
class CanBeStoppedUseCase : UseCase<CanBeStoppedUseCase.InnerRequestValue, CanBeStoppedUseCase.InnerResponseValue>()
    ,DefaultLifecycleObserver {

    private val downloadFile = DownloadFile()
    private val repository = WanAndroidRepository

    class InnerRequestValue : RequestValues {

    }

    class InnerResponseValue(val dataResult: DataResult<DownloadFile>) : ResponseValue {

    }


    override fun executeUseCase(requestValues: InnerRequestValue?) {
        //访问数据层资源，在 UseCase 中处理带叫停性质的业务
        repository.downLoadFile(downloadFile, object : DownloadFile.DownLoadResult {
            override fun onResult(downloadFile: DataResult<DownloadFile>) {
                useCaseCallback.onSuccess(InnerResponseValue(downloadFile))
            }
        })
    }

    override fun onStop(owner: LifecycleOwner) {
        if(requestValues!=null){
            downloadFile.process = 0
            downloadFile.forgive = true
            downloadFile.file = null
            useCaseCallback.onError()
        }
    }

}