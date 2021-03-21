package com.bsnl.sample.pkg

import android.content.Context
import com.bsnl.base.utils.ApiUtils
import com.bsnl.sample.export.api.SampleApi
import com.bsnl.sample.export.api.SampleParam
import com.bsnl.sample.pkg.feature.view.SampleActivity
import com.bsnl.sample.pkg.feature.view.async.AsyncCreateViewActivity
import com.bsnl.sample.pkg.feature.view.databinding.DataBindingSampleActivity
import com.bsnl.sample.pkg.feature.view.login.LoginActivity
import com.bsnl.sample.pkg.feature.view.viewpager.ViewPagerActivity
import com.bsnl.sample.pkg.feature.view.webview.WebViewActivity

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

    override fun startLoginActivity(context: Context) {
        LoginActivity.startAction(context)
    }

    override fun startWebViewActivity(context: Context, url: String) {
        WebViewActivity.startAction(context, url)
    }

    override fun startViewPagerActivity(context: Context) {
        ViewPagerActivity.startAction(context)
    }

    override fun startAsyncCreateViewActivity(context: Context) {
        AsyncCreateViewActivity.startAction(context)
    }

    override fun getSampleParam(): SampleParam {
        return SampleParam("sample")
    }

    override fun startDataBindingActivity(context: Context) {
        DataBindingSampleActivity.startAction(context)
    }
}