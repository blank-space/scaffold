package com.dawn.base.ui.page.delegate

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dawn.base.log.L
import com.dawn.base.ui.page.iface.IRefreshLayout
import com.dawn.base.ui.page.iface.OnRefreshAndLoadMoreListener
import com.dawn.base.ui.page.iface.RefreshType
import com.dawn.base.ui.page.delegate.iface.IListViewDelegate
import com.dawn.base.widget.refreshLayout.RefreshLayoutProxy
import com.dawn.base.utils.RecyclerViewUtil
import com.dawn.base.viewmodel.base.BaseListViewModel
import com.dawn.base.viewmodel.iface.RequestType
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/11
 * @desc   :
 */
class ListViewDelegateImpl(val viewModel: BaseListViewModel, private val owner: LifecycleOwner) :
    IListViewDelegate {

    private val mAdapter by lazy {
        MultiTypeAdapter(viewModel.providerData())
    }
    private var mRecyclerView: RecyclerView? = null
    var mRefreshLayout: RefreshLayoutProxy? = null
    private var mILoadDataFinishListener: IListViewDelegate.IDoExtendListener? = null

    override fun initRecyclerView(rv: RecyclerView?) {
        mRecyclerView = rv
        RecyclerViewUtil.initRecyclerView(mRecyclerView, mAdapter)
    }

    @ExperimentalCoroutinesApi
    override fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout?) {
        if (smartRefreshLayout == null) {
            L.e("smartRefreshLayout cant be null !")
        }
        mRefreshLayout =
            RefreshLayoutProxy(smartRefreshLayout, object : OnRefreshAndLoadMoreListener {
                override fun onRefresh(refreshLayout: IRefreshLayout?) {
                    loadData(RequestType.REFRESH)
                }

                override fun onLoadMore(refreshLayout: IRefreshLayout?) {
                    loadData(RequestType.LOAD_MORE)
                }

            })
        processRefreshType(getRefreshType())
    }

    override fun setRefreshProxy(proxy: RefreshLayoutProxy?) {
        mRefreshLayout = proxy
    }

    override fun observeLiveDataCallback() {
        //完成刷新
        viewModel.finishRefresh.observe(owner){
            if (it) {
                mRefreshLayout?.finishRefresh()
            }
        }
        //完成加载更多
        viewModel.finishLoadMore.observe(owner) {
            if (it) {
                mRefreshLayout?.finishLoadMore()
            }
        }
        //是否能加载更多
        viewModel.enableLoadMore.observe(owner) {
            if (it) {
                mRefreshLayout?.setEnableLoadMore(it)
            }

        }
        //是否没有更多数据
        viewModel.noMoreData.observe(owner) {
            mRefreshLayout?.setNoMoreData(it)
        }
        //刷新数据
        viewModel.notifyDataChange.observe(owner) {
            if (it > 0) {
                mAdapter.notifyItemInserted(it)
            } else {
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun processRefreshType(refreshType: Int) {
        if (mRefreshLayout == null) {
            L.e("mRefreshLayout == null")
            return
        }
        when (refreshType) {
            RefreshType.REFRESH_AND_LOAD_MORE -> {
                mRefreshLayout?.setEnableRefresh(true)
                mRefreshLayout?.setEnableLoadMore(true)
            }
            RefreshType.REFRESH_ONLY -> {
                mRefreshLayout?.setEnableRefresh(true)
                mRefreshLayout?.setEnableLoadMore(false)
            }
            RefreshType.LOAD_MORE_ONLY -> {
                mRefreshLayout?.setEnableRefresh(false)
                mRefreshLayout?.setEnableLoadMore(true)
            }
            else -> {
                mRefreshLayout?.setEnableRefresh(false)
                mRefreshLayout?.setEnableLoadMore(false)
            }
        }
    }

    override fun getAdapter(): MultiTypeAdapter {
        return mAdapter
    }

    override fun getRefreshType(): Int {
        return RefreshType.REFRESH_AND_LOAD_MORE
    }

    /**
     * initData()在BaseListViewModel#init{}里只执行一次，即使是生命周期重建也不会二次请求
     */
    override fun initData(){
        viewModel.resultLiveData?.observe(owner){
            mILoadDataFinishListener?.apply {
                this.onLoadDataFinish(it?.data)
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun loadData(requestType: Int) {
        viewModel.fetchListData(requestType)?.observe(owner) {
            mILoadDataFinishListener?.apply {
                this.onLoadDataFinish(it?.data)
            }
        }
    }

    override fun getRecyclerView(): RecyclerView? = mRecyclerView

    override fun getRefreshLayoutProxy(): RefreshLayoutProxy? = mRefreshLayout

    override fun setILoadDataFinishListener(lsn: IListViewDelegate.IDoExtendListener) {
        mILoadDataFinishListener = lsn
    }
}