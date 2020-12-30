package com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool


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

    override fun release(layoutId: Int?, instance: T): Boolean {
        synchronized(mLock) {
            return super.release(layoutId, instance)
        }
    }

    override fun acquire(layoutId: Int?): T? {
        synchronized(mLock) {
            return super.acquire(layoutId)
        }
    }
}