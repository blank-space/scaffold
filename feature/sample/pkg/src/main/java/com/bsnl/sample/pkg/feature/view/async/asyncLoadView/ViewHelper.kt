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
 * @note:   1）使用异步 Inflate+全局缓存构建的 View，在使用时需要重新设置 LayoutParams，不然显示上可能不是最终想要的结果；
 *          2）使用异步 Inflate+全局缓存构建的 View，如果 View 的解析过程中，存在 Theme 相关的，可能会导致 View 构建失败。如原生的 TabLayout，解析时会读取 Theme 中的属性，如果 context 传入的是 Application 且没有设置相关 Theme，就会报错；
 *          3）使用异步 Inflate+全局缓存构建的 View，需要及时的调用 refreshCurrentActivity 方法，这样尽可能的保持 context 是当前的 activity 实例。在使用 context 的时候，避免直接把 context 强转 activity，而是使用 ViewContext 的 getActivity 方法获取。
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
                val view =
                    LayoutInflater.from(viewContext?.getCurrentContext()).inflate(layoutId, null)
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