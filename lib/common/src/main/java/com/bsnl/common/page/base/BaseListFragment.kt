package com.bsnl.common.page.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bsnl.common.R
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.page.delegate.iface.IListViewDelegate
import com.bsnl.common.page.delegate.ListViewDelegateImpl
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.RequestType

import com.drakeet.multitype.MultiTypeAdapter

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.lib_common_recycerview.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 基础列表fragment
 *
 */
abstract class BaseListFragment<T : BaseListViewModel> : BaseFragment<T>() {

    private var mListViewDelegate: ListViewDelegateImpl? = null

    abstract fun registerItem(adapter: MultiTypeAdapter?)

    //初始化代理
    protected fun setupListViewDelegate() {
        mListViewDelegate = ListViewDelegateImpl(mViewModel as BaseListViewModel, this)
        mListViewDelegate?.setILoadDataFinishListener(object :
            IListViewDelegate.IDoExtendListener {
            override fun onLoadDataFinish(data: Any?) {
                onGetDataFinish(data)
            }
        })
        mListViewDelegate?.initRecyclerView(rv)
        registerItem(mListViewDelegate?.getAdapter())
        mListViewDelegate?.setRefreshProxy(getLayoutDelegateImpl()?.getRefreshLayout())
    }

    override fun getLayoutId(): Int = R.layout.lib_common_recycerview

    override fun initView(v: View) {
        setupListViewDelegate()
    }

    override fun getRefreshLayout(): SmartRefreshLayout? {
        return getLayoutDelegateImpl()?.getRefreshLayout()?.getSmartRefreshLayout()
    }

    protected fun fetchData() {
        mListViewDelegate?.loadData(RequestType.INIT)
    }

    override fun initData() {
        fetchData()
    }


    /**
     * 监听LiveData的通知
     */
    protected fun setupLiveDataCallback() {
        mListViewDelegate?.observeLiveDataCallback()
    }

    override fun initListener() {
        super.initListener()
        setupLiveDataCallback()
    }


    /**
     * 子类可以复写刷新类型，默认支持刷新和加载更多
     *
     * @return RefreshType
     */
    protected override fun getRefreshType(): Int {
        mListViewDelegate?.apply {
            return getRefreshType()
        }
        return RefreshType.REFRESH_AND_LOAD_MORE
    }


    protected open fun onGetDataFinish(data: Any?) {}

    fun getRecyclerView(): RecyclerView? = mListViewDelegate?.getRecyclerView()


    fun getAdapter(): MultiTypeAdapter? = mListViewDelegate?.getAdapter()

    override fun processLoadMore(refreshLayout: IRefreshLayout?) {
        mListViewDelegate?.loadData(RequestType.LOAD_MORE)
    }

    override fun processRefresh(refreshLayout: IRefreshLayout?) {
        mListViewDelegate?.loadData(RequestType.REFRESH)
    }

    override fun onPageReload(v: View?) {
        mListViewDelegate?.loadData(RequestType.INIT)
    }


}