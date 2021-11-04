package com.dawn.sample.pkg

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.log.L
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.feature.constant.BUNDLE_URL

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
@Route(path = SamplePath.S_SAMPLE_SERVICE)
class SampleApiImpl : ISampleService {

    override fun startWebViewActivity( url: String) {
        ARouter.getInstance().build(SamplePath.A_WEBVIEW_ACTIVITY).withString(BUNDLE_URL,url).navigation()
    }

    override fun startListViewActivity() {
        ARouter.getInstance().build(SamplePath.A_LISTVIEW_ACTIVITY).navigation()
    }

    override fun startFindLocationActivity() {
        ARouter.getInstance().build(SamplePath.A_FIND_LOCATION_ACTIVITY).navigation()
    }

    override fun startSearchActivity() {
        ARouter.getInstance().build(SamplePath.A_SEARCH_ACTIVITY).navigation()
    }

    override fun startHexStatusManagerActivity() {
        ARouter.getInstance().build(SamplePath.A_HEX_STATUS_MANAGER_ACTIVITY).navigation()
    }

    override fun init(context: Context?) {
    }


}