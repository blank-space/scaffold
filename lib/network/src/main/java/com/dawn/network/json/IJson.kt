package com.dawn.network.json

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/8
 * @desc   :
 */
interface IJson {

    fun <T> convertJsonToClass(clz: Class<T>, json: String): T?

    fun <T> convertJsonToList(clz: Class<T>, json: String): List<T>?

    fun <T> convertClassToJson(t: T, clazz: Class<T>): String

    fun <T> convertListToJson(t: List<T>, clazz: Class<T>): String
}