package com.dawn.network.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/7
 * @desc   :
 */
object JsonUtil : IJson {
    val build: Moshi = Moshi.Builder().build()

    override fun <T> convertJsonToClass(clz: Class<T>, json: String): T? {
        val adapter = build.adapter<T>(clz)
        return adapter.fromJson(json)
    }

    override fun <T> convertJsonToList(clz: Class<T>, json: String): List<T>? {
        val listType = Types.newParameterizedType(List::class.java, clz)
        val adapter = build.adapter<List<T>>(listType)
        return adapter.fromJson(json)
    }

    override fun <T> convertClassToJson(t: T, clazz: Class<T>): String {
        val jsonAdapter = build.adapter(clazz)
        return jsonAdapter.toJson(t)
    }

    override fun <T> convertListToJson(t: List<T>, clazz: Class<T>): String {
        val listType = Types.newParameterizedType(List::class.java, clazz)
        val adapter = build.adapter<List<T>>(listType)
        return adapter.toJson(t)
    }
}