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

    abstract fun startLoginActivity(context: Context)

    abstract fun startWebViewActivity(context: Context, url: String)

    abstract fun startViewPagerActivity(context: Context)

    abstract  fun  startAsyncCreateViewActivity(context: Context)

    abstract fun getSampleParam():SampleParam


}

class SampleParam(var name: String)

class SampleResult(var name: String)