package com.dawn.network.gson

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/7
 * @desc   :
 */
object JsonUtil {
    val build: Moshi = Moshi.Builder().build()

    fun <T> convertJsonToClass(clz: Class<T>, json: String): T? {
        val adapter = build.adapter<T>(clz)
        return adapter.fromJson(json)
    }

    fun <T> convertJsonToListClass(clz: Class<T>, json: String): List<T>? {
        val listType = Types.newParameterizedType(List::class.java, clz)
        val adapter = build.adapter<List<T>>(listType)
        return adapter.fromJson(json)
    }

    fun <T> convertClassToJson(t: T, clazz: Class<T>): String {
        val jsonAdapter = build.adapter(clazz)
        return jsonAdapter.toJson(t)
    }

    fun <T> convertListToJson(t: List<T>, clazz: Class<T>): String {
        val listType = Types.newParameterizedType(List::class.java, clazz)
        val adapter = build.adapter<List<T>>(listType)
        return adapter.toJson(t)
    }
}