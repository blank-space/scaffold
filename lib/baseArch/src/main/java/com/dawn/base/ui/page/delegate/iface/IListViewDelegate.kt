package com.dawn.base.ui.page.delegate.iface

import androidx.recyclerview.widget.RecyclerView
import com.dawn.base.ui.page.iface.RefreshType
import com.dawn.base.widget.refreshLayout.RefreshLayoutProxy
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/11
 * @desc   : 代理列表页请求数据
 */
interface IListViewDelegate {

    /**
     * 设置RV
     */
    fun initRecyclerView(rv: RecyclerView?)

    /**
     * 设置刷新、加载更多的回调
     */
    fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout?)

    fun setRefreshProxy(proxy: RefreshLayoutProxy?)


    /**
     * 监听LiveData的通知
     */
    fun observeLiveDataCallback()

    /**
     *  根据刷新类型设置是否可以刷新或加载更多
     */
    fun processRefreshType(@RefreshType.Val refreshType: Int)


    fun getAdapter(): MultiTypeAdapter

    fun getRefreshType(): Int

    fun initData()

    fun loadData(@RefreshType.Val requestType: Int)

    fun getRecyclerView(): RecyclerView?

    fun getRefreshLayoutProxy(): RefreshLayoutProxy?


    /**
     * 通过接口回调对外部提供拓展
     */
    interface IDoExtendListener {

        fun onLoadDataFinish(data: Any?)
    }

    fun setILoadDataFinishListener(lsn: IDoExtendListener)
}