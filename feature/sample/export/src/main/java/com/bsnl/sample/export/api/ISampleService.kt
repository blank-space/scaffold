package com.bsnl.sample.export.api

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
interface ISampleService : IProvider {

     fun startWebViewActivity(url: String)

     fun startListViewActivity()

}

class SampleParam(var name: String)

class SampleResult(var name: String)