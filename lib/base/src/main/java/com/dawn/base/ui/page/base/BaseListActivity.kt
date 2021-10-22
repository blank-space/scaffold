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
import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.base.viewmodel.iface.RequestType
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 基础列表Activity
 *
 */
abstract class BaseListActivity<T : BaseViewModel,VB : ViewBinding> : BaseBindingActivity<T, VB>() {
    private var mListViewDelegate: ListViewDelegateImpl? = null

    private val rv :RecyclerView by lazy {
        binding.root.findViewById(R.id.rv)
    }

    abstract fun registerItem(adapter: MultiTypeAdapter?)

    override fun initView() {
        setupListViewDelegate()
    }

    protected fun setupListViewDelegate() {
        //初始化代理
        mListViewDelegate = ListViewDelegateImpl(mViewModel as BaseListViewModel, this)
        mListViewDelegate?.setILoadDataFinishListener(object :
            IListViewDelegate.IDoExtendListener {
            override fun onLoadDataFinish(data: Any?) {
                onGetDataFinish(data)
            }
        })
        registerItem(mListViewDelegate?.getAdapter())
        mListViewDelegate?.initRecyclerView(rv)
        mListViewDelegate?.setupRefreshLayout(getLayoutDelegateImpl()?.getRefreshLayout())
    }

    fun getRecyclerView(): RecyclerView? = mListViewDelegate?.getRecyclerView()

    fun getAdapter(): MultiTypeAdapter? = mListViewDelegate?.getAdapter()

    override fun getRefreshLayout(): SmartRefreshLayout? {
        return getLayoutDelegateImpl()?.getRefreshLayout()
    }


    override fun initData() {
        mListViewDelegate?.loadData(RequestType.INIT)
    }

    override fun initListener() {
        super.initListener()
        //监听LiveData的通知
        mListViewDelegate?.observeLiveDataCallback()
    }

    /**
     * 子类可以复写刷新类型，默认支持刷新和加载更多
     *
     * @return RefreshType
     */
    override fun getRefreshType(): Int {
        mListViewDelegate?.apply {
            return getRefreshType()
        }
        return RefreshType.REFRESH_AND_LOAD_MORE
    }

    /**
     * 提供给外部做一些额外的处理
     */
    protected open fun onGetDataFinish(data: Any?) {}

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