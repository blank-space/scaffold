package com.dawn.sample.export.api

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
interface ISampleService : IProvider {


     fun startListViewActivity()

     fun startFindLocationActivity()

     fun startSearchActivity()

     fun startHexStatusManagerActivity()

}

class SampleParam(var name: String)

class SampleResult(var name: String)