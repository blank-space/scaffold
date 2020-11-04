package com.bsnl.base.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/12
 * @desc   : 轻量级数据存储
 */
object MMKVUtil {
    private val mKv by lazy {
        MMKV.defaultMMKV()
    }


    fun put(key: String?, value: Any) {
        when {
            value is Int -> mKv.encode(key, value)
            value is String -> mKv.encode(key, value)
            value is Boolean -> mKv.encode(key, value)
            value is Float -> mKv.encode(key, value)
            value is Long -> mKv.encode(key, value)
            value is Double -> mKv.encode(key, value)
            value is Parcelable -> mKv.encode(key, value)
        }
    }


    fun getString(k: String?): String {
        return getString(k, "")
    }

    fun getString(k: String?, def: String?): String {
        return mKv.decodeString(k, def)
    }

    fun getInt(k: String?): Int {
        return mKv.decodeInt(k, 0)
    }

    fun getInt(k: String?,v :Int): Int {
        return mKv.decodeInt(k, v)
    }

    fun getBoolean(k: String?): Boolean {
        return mKv.decodeBool(k, false)
    }

    fun getFloat(k: String?): Float {
        return mKv.decodeFloat(k, 0.0f)
    }

    fun getLong(k: String?): Long {
        return mKv.decodeLong(k, 0L)
    }

    fun getDouble(k: String?): Double {
        return mKv.decodeDouble(k)
    }

    fun <T : Parcelable> getParcelable(k: String?, clz: Class<T>): T {
        return mKv.decodeParcelable(k, clz)
    }


}