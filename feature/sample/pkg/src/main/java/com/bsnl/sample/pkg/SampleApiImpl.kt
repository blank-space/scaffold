package com.bsnl.sample.pkg

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.base.log.L
import com.bsnl.sample.export.api.ISampleService
import com.bsnl.sample.export.path.SamplePath
import com.bsnl.sample.pkg.feature.constant.Bundle_TITLE
import com.bsnl.sample.pkg.feature.constant.Bundle_URL

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
@Route(path = SamplePath.S_SAMPLE_SERVICE)
class SampleApiImpl : ISampleService {

    override fun startWebViewActivity( url: String) {
        ARouter.getInstance().build(SamplePath.A_WEBVIEW_ACTIVITY).withString(Bundle_URL,url).navigation()
    }

    override fun startListViewActivity() {
        ARouter.getInstance().build(SamplePath.A_LISTVIEW_ACTIVITY).navigation()
    }

    override fun init(context: Context?) {
        L.d("init sampleService")
    }


}