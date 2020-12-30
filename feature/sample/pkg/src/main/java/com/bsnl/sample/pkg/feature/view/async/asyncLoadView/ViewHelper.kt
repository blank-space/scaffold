package com.bsnl.sample.pkg.feature.view.async.asyncLoadView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.bsnl.common.utils.replaceLooperWithMainThreadQueue
import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool.SynchronizedPool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author : LeeZhaoXing
 * @date   : 2020/12/29
 * @desc   :
 */
class ViewHelper private constructor() {
    private val mViewPool = SynchronizedPool<View>(10)

    interface IAsyncLoadView {
        fun generatedView(v: View)
    }

    companion object {
        private var viewContext: ViewContext? = null
        private var mInstance: ViewHelper? = null
            get() {
                return field ?: ViewHelper()
            }

        fun getInstance(ctx: Context): ViewHelper {
            viewContext = ViewContext(ctx)
            return mInstance!!
        }
    }

    /**
     * 刷新ViewContext中的context
     */
    fun refreshCurrentActivity(ctx: Context) {
        requireNotNull(viewContext, { "viewContext cannot null" })
        viewContext?.setCurrentContext(ctx)
    }

    /**
     * 异步加载view，并且放入缓存池
     * [layoutId,View]
     */
    fun asyncPreLoadView(layoutId: Int, listener: IAsyncLoadView? = null) {
        GlobalScope.launch(Dispatchers.IO) {
            if (replaceLooperWithMainThreadQueue(false)) {
                val view = LayoutInflater.from(viewContext?.getCurrentContext()).inflate(layoutId, null)
                replaceLooperWithMainThreadQueue(true)
                mViewPool.release(layoutId, view)
                listener?.generatedView(view)

            }
        }
    }

    /**
     * 通过layoutId获取缓存view
     */
    fun getView(layoutId: Int): View? {
        return mViewPool.acquire(layoutId)
    }

    /**
     * 通过layoutId回收view
     */
    fun recycleView(layoutId: Int) {
        var view = mViewPool.recycle(layoutId);
        view = null
    }


}