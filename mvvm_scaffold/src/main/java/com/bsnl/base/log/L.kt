package com.bsnl.base.log

import com.bsnl.mvvm.scaffold.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/15
 * @desc   : 日志
 */
object L {

    private val TAG = "TAG"

    /**
     * 在[Application#onCreate()]里调用该方法
     */
    fun init() {
        if (BuildConfig.LOG_DEBUG) {
            Timber.plant(DebugTree())
        }
    }


    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        Timber.tag(tag).d(msg)
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        Timber.tag(tag).e(msg)
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun i(tag: String, msg: String) {
        Timber.tag(tag).i(msg)
    }

    fun v(msg: String) {
        v(TAG, msg)
    }

    fun v(tag: String, msg: String) {
        Timber.tag(tag).v(msg)
    }

    fun w(msg: String) {
        w(TAG, msg)
    }

    fun w(tag: String, msg: String) {
        Timber.tag(tag).w(msg)
    }


}