package com.bsnl.databinding

import androidx.recyclerview.widget.RecyclerView
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.page.delegate.iface.IListViewDelegate
import com.bsnl.common.page.delegate.ListViewDelegateImpl
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.RequestType

import com.drakeet.multitype.MultiTypeAdapter

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.lib_databinding_refreshlayout.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 基础列表fragment ， 子类布局文件必须include lib_databinding_refreshlayout.xml
 *
 */
abstract class ListDataBindingFragment<T : BaseListViewModel> : DataBindingFragment<T>() {

    private var mListViewDelegate: ListViewDelegateImpl? = null

    abstract fun registerItem(adapter: MultiTypeAdapter?)

    //初始化代理
    protected fun setupListViewDelegate(){
        mListViewDelegate = ListViewDelegateImpl(mViewModel as BaseListViewModel, this)
        mListViewDelegate?.setILoadDataFinishListener(object :
            IListViewDelegate.IDoExtendListener {
            override fun onLoadDataFinish(data: Any?) {
                onGetDataFinish(data)
            }
        })
        mListViewDelegate?.initRecyclerView(recyclerview)
        registerItem(mListViewDelegate?.getAdapter())
    }

    override fun initView() {
       setupListViewDelegate()
    }

    override fun getRefreshLayout(): SmartRefreshLayout? {
        return refreshLayout
    }

    protected fun fetchData() {
        mListViewDelegate?.loadData(RequestType.INIT)
    }

    override fun initData() {
        fetchData()
    }

    /**
     * 设置刷新、加载更多
     */
    protected fun setupRefreshCallback(){
        mListViewDelegate?.setupRefreshLayout(getRefreshLayout())
    }

    /**
     * 监听LiveData的通知
     */
    protected fun setupLiveDataCallback(){
        mListViewDelegate?.observeLiveDataCallback()
    }

    override fun initListener() {
        super.initListener()
        setupRefreshCallback()
        setupLiveDataCallback()
    }


    /**
     * 子类可以复写刷新类型，默认支持刷新和加载更多
     *
     * @return RefreshType
     */
    protected open fun getRefreshType(): Int {
        mListViewDelegate?.apply {
            return getRefreshType()
        }
        return RefreshType.REFRESH_AND_LOAD_MORE
    }


    protected open fun onGetDataFinish(data: Any?) {}

    fun getRecyclerView(): RecyclerView? = mListViewDelegate?.getRecyclerView()


    fun getAdapter(): MultiTypeAdapter? = mListViewDelegate?.getAdapter()


}