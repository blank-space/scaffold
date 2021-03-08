package com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool

import com.bsnl.base.log.L
import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool.iface.Pool
import java.util.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/30
 * @desc   : 使用HashMap维持索引的对象池
 */
open class SimplePool<T>(maxPoolSize: Int) : Pool<T> {
    var mPool: HashMap<Int, T>
    private var mPoolSize = 0
    private var mMaxSize = 0
    init {
        require(maxPoolSize > 0) { "The max pool size must be > 0" }
        mMaxSize = maxPoolSize
        mPool = HashMap()
    }

    override fun get(layoutId: Int?): T? {
        if (layoutId == null || layoutId <= 0) {
            return null
        }
        if (mPool.size > 0) {
            mPoolSize--
            return mPool!![layoutId]
        }
        return null
    }

    override fun put(layoutId: Int?, instance: T): Boolean {
        if (layoutId == null || layoutId <= 0) {
            return false
        }
        if(isInPool(layoutId)){
            L.e( "Already in the pool!" )
            return false
        }
        if (mPoolSize < mMaxSize) {
            mPool[layoutId] = instance
            mPoolSize++
            return true
        }
        return false
    }

    private fun isInPool(layoutId: Int): Boolean {
        for (key in mPool.keys) {
            if (key == layoutId) {
                return true
            }
        }
        return false
    }
    override fun recycle(layoutId: Int?): T? {
        if (layoutId == null || layoutId <= 0) {
            return null
        }
        if (mPoolSize > 0) {
            mPoolSize--
            return mPool.remove(layoutId)
        }
        return null
    }
}