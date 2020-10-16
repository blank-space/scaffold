package com.dawn.wan.data.model

/**
 * @author : LeeZhaoXing
 * @date   : 2020/5/14
 * @desc   :
 */
data class Response< T>(
    val errorCode: Int,
    val errorMsg: String,
    var data: T
)