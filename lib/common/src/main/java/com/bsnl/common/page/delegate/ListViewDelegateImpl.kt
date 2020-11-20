package com.bsnl.common.page.delegate

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bsnl.base.log.L
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.OnRefreshAndLoadMoreListener
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.page.delegate.iface.IListViewDelegate
import com.bsnl.common.refreshLayout.RefreshLayoutProxy
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.RequestType
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/11
 * @desc   :
 */
class ListViewDelegateImpl(val viewModel: BaseListViewModel, private val owner: LifecycleOwner) :
    IListViewDelegate {
    init {
        requireNotNull(viewModel, { " viewModel cannot be null" })
        requireNotNull(owner, { " owner cannot be null" })
    }

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


    override fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout?) {
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
        viewModel.finishRefresh.observe(owner, Observer {
            if (it) {
                mRefreshLayout?.finishRefresh()
            }
        })
        //完成加载更多
        viewModel.finishLoadMore.observe(owner, Observer {
            if (it) {
                mRefreshLayout?.finishLoadMore()
            }
        })
        //是否能加载更多
        viewModel.enableLoadMore.observe(owner, Observer {
            if (it) {
                mRefreshLayout?.setEnableLoadMore(it)
            }

        })
        //是否没有更多数据
        viewModel.noMoreData.observe(owner, Observer {
            mRefreshLayout?.setNoMoreData(it)
        })
        //刷新数据
        viewModel.notifyDataChange.observe(owner, Observer {
            if (it > 0) {
                mAdapter.notifyItemInserted(it)
            } else {
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun processRefreshType(refreshType: Int) {
        if (mRefreshLayout == null) {
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

    override fun loadData(requestType: Int) {
        viewModel.fetchListData(requestType)?.observe(owner, Observer {
            mILoadDataFinishListener?.apply {
                this.onLoadDataFinish(it?.data)
            }
        })
    }


    override fun getRecyclerView(): RecyclerView? = mRecyclerView


    override fun getRefreshLayoutProxy(): RefreshLayoutProxy? = mRefreshLayout

    override fun setILoadDataFinishListener(lsn: IListViewDelegate.IDoExtendListener) {
        mILoadDataFinishListener = lsn
    }
}