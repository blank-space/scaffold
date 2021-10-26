package com.dawn.sample.pkg.feature.viewmodel

import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.sample.pkg.feature.domain.request.DownloadRequest

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/26
 * @desc   :
 */
class DownLoadViewModel : BaseViewModel() {
    val downloadRequest = DownloadRequest()
}