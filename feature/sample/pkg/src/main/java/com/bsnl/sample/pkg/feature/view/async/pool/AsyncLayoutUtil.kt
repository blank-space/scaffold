package com.bsnl.sample.pkg.feature.view.async.pool

import android.view.View

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/28
 * @desc   :
 */
object AsyncLayoutUtil {
     val mRequestPool = Pools.SynchronizedPool<View>(10)
}