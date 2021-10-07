package com.bsnl.common.page.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bsnl.common.R
import com.bsnl.common.databinding.CommonRecycerviewBinding
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.page.delegate.ListViewDelegateImpl
import com.bsnl.common.page.delegate.iface.IListViewDelegate
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.BaseViewModel
import com.bsnl.common.viewmodel.RequestType
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.common_recycerview.*


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 最简单的LitView-Activity,如果布局比较复杂，可以使用[BaseBindingActivity]
 *
 */
abstract class SimpleListActivity<T : BaseViewModel> : BaseBindingActivity<T, CommonRecycerviewBinding>() {
    private var mListViewDelegate: ListViewDelegateImpl? = null

    abstract fun registerItem(adapter: MultiTypeAdapter?)

    override fun initView() {
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
        mListViewDelegate?.setRefreshProxy(getLayoutDelegateImpl()?.getRefreshLayout())

    }

    fun getRecyclerView(): RecyclerView? = mListViewDelegate?.getRecyclerView()


    fun getAdapter(): MultiTypeAdapter? = mListViewDelegate?.getAdapter()



    override fun getRefreshLayout(): SmartRefreshLayout? {
        return getLayoutDelegateImpl()?.getRefreshLayout()?.getSmartRefreshLayout()
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