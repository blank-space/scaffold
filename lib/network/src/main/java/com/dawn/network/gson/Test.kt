package com.dawn.network.gson

import com.squareup.moshi.Moshi

/**
 * @author : LeeZhaoXing
 * @date : 2021/11/7
 * @desc :
 */
class Test {
    fun <T> getJsonString(t: T, clazz: Class<T>): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(clazz)
        return jsonAdapter.toJson(t)
    }
}