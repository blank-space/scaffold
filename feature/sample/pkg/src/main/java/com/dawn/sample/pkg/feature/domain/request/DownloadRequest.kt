package com.dawn.sample.pkg.feature.domain.request

import com.dawn.base.DataResult
import com.dawn.domain.request.BaseRequest
import com.dawn.domain.usecase.UseCaseHandler
import com.dawn.sample.pkg.feature.data.entity.DownloadFile
import com.dawn.sample.pkg.feature.domain.usecase.CanBeStoppedUseCase
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/26
 * @desc   : request的职责仅限于对数据请求的转发，不建议在此处理UI逻辑，
 */
class DownloadRequest : BaseRequest() {

    private val downloadFileCanBeStoppedLiveData = UnPeekLiveData<DataResult<DownloadFile>>()
    val downloadLiveData: ProtectedUnPeekLiveData<DataResult<DownloadFile>> = downloadFileCanBeStoppedLiveData
    val canBeStoppedUseCase = CanBeStoppedUseCase()

    fun requestCanBeStoppedDownloadFile() {
        UseCaseHandler.getInstance()
            .execute(canBeStoppedUseCase, CanBeStoppedUseCase.InnerRequestValue()) {
                downloadFileCanBeStoppedLiveData.value = it.dataResult
            }
    }
}