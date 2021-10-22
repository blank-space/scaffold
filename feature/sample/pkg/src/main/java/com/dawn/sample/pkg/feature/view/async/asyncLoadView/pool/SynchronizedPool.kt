package com.dawn.sample.pkg.feature.view.async.asyncLoadView.pool


/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/30
 * @desc   : 线程安全的对象池
 */
class SynchronizedPool<T>(maxPoolSize: Int) : SimplePool<T>(maxPoolSize) {
    private val mLock = Any()
    override fun recycle(layoutId: Int?): T? {
        synchronized(mLock) {
            return super.recycle(layoutId)
        }
    }

    override fun put(layoutId: Int?, instance: T): Boolean {
        synchronized(mLock) {
            return super.put(layoutId, instance)
        }
    }

    override fun get(layoutId: Int?): T? {
        synchronized(mLock) {
            return super.get(layoutId)
        }
    }
}