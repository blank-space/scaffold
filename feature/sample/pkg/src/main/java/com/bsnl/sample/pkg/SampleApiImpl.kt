package com.bsnl.sample.pkg

import android.content.Context
import com.bsnl.base.utils.ApiUtils
import com.bsnl.sample.export.api.SampleApi
import com.bsnl.sample.pkg.feature.view.SampleActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */

@ApiUtils.Api
class SampleApiImpl : SampleApi() {

    override fun startSampleActivity(context: Context) {
        SampleActivity.actionStart(context)
    }
}