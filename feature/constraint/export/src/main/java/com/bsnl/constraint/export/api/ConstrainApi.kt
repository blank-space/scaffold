package com.bsnl.constraint.export.api

import android.content.Context
import com.bsnl.base.utils.ApiUtils

/**
 * @author : LeeZhaoXing
 * @date   : 2021/1/4
 * @desc   :
 */
abstract class ConstrainApi : ApiUtils.BaseApi(){
    abstract fun startSampleActivity(context: Context)
}