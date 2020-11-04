package com.bsnl.sample.export.api

import android.content.Context
import com.bsnl.base.utils.ApiUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
abstract class SampleApi : ApiUtils.BaseApi() {

    abstract fun startSampleActivity(context: Context)

    abstract fun startFpsMonitorActivity(context: Context)
}