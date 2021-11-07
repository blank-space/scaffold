package com.dawn.sample.pkg.feature.data.entity

import com.squareup.moshi.JsonClass

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/5
 * @desc   :
 */
@JsonClass(generateAdapter = true)
data class LoginParam(val username:String,val password:String)
