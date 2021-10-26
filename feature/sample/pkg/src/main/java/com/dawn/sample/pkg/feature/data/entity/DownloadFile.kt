package com.dawn.sample.pkg.feature.data.entity

import com.dawn.base.DataResult
import java.io.File

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/26
 * @desc   :
 */
data class DownloadFile(var process: Int = 0, var file: File?=null, var forgive: Boolean= false) {
    interface DownLoadResult {
        fun onResult(downloadFile: DataResult<DownloadFile>)
    }
}
