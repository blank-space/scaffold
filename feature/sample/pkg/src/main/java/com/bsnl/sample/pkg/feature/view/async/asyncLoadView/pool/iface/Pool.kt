package com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool.iface

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/30
 * @desc   :
 */
open interface Pool<T> {
    /**
     * 从缓存池取出
     */
    fun acquire(layoutId: Int?): T?

    /**
     * 放入缓存池
     *
     * @param instance The instance to release.
     * @return Whether the instance was put in the pool.
     * @throws IllegalStateException If the instance is already in the pool.
     */
    fun release(layoutId: Int?, instance: T): Boolean

    /**
     * 从缓存池取出并移除
     */
    fun recycle(layoutId: Int?): T?
}