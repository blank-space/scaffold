package com.dawn.base.ui.page.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dawn.base.R
import com.dawn.base.ui.page.iface.IRefreshLayout
import com.dawn.base.ui.page.iface.RefreshType
import com.dawn.base.ui.page.delegate.ListViewDelegateImpl
import com.dawn.base.ui.page.delegate.iface.IListViewDelegate
import com.dawn.base.viewmodel.base.BaseListViewModel
import com.dawn.base.viewmodel.iface.RequestType
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 基础列表fragment
 *
 */
abstract class BaseListFragment<T : BaseListViewModel, VB : ViewBinding> : BaseFragment<T, VB>() {
    private val rv: RecyclerView by lazy {
        binding.root.findViewById(R.id.rv)
    }
    private var mListViewDelegate: ListViewDelegateImpl? = null

    abstract fun registerItem(adapter: MultiTypeAdapter?)

    //初始化代理
    open fun setupListViewDelegate() {
        mListViewDelegate =
            ListViewDelegateImpl(
                mViewModel as BaseListViewModel,
                viewLifecycleOwner,
                getRefreshType()
            )
        mListViewDelegate?.setILoadDataFinishListener(object :
            IListViewDelegate.IDoExtendListener {
            override fun onLoadDataFinish(data: Any?) {
                onGetDataFinish(data)
            }
        })
        mListViewDelegate?.initRecyclerView(rv)
        registerItem(mListViewDelegate?.getAdapter())
        mListViewDelegate?.setupRefreshLayout(getLayoutDelegateImpl()?.getRefreshLayout())
    }


    override fun initView(v: View) {
        setupListViewDelegate()
    }

    override fun getRefreshLayout(): SmartRefreshLayout? {
        return getLayoutDelegateImpl()?.getRefreshLayout()
    }

    protected fun fetchData() {
        mListViewDelegate?.initData()
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
    override fun getRefreshType(): Int {
        return RefreshType.REFRESH_AND_LOAD_MORE
    }


    protected open fun onGetDataFinish(data: Any?) {}

    fun getRecyclerView(): RecyclerView = rv


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
