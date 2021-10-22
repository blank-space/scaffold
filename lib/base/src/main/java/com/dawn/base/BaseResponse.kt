package com.dawn.base

import com.google.gson.annotations.SerializedName

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
data class BaseResponse<T>(
    @SerializedName("errorCode")
    val errorCode: Int = 0,
    @SerializedName("errorMsg")
    val errorMsg: String? = null,
    @SerializedName("data")
    val data: T? = null
)
