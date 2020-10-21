package com.bsnl.common.dataBinding

import androidx.lifecycle.Observer
import com.bsnl.common.dataBinding.DataBindingFragment
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.OnRefreshAndLoadMoreListener
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.refreshLayout.RefreshLayoutProxy
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.BaseViewModel
import com.bsnl.common.viewmodel.RequestType

import com.drakeet.multitype.MultiTypeAdapter

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.lib_common_refreshlayout.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 基础列表fragment ， 子类布局文件必须include lib_common_refreshlayout.xml
 *
 */
abstract class BaseListDataBindingFragment<T : BaseViewModel> : DataBindingFragment<T>() {

    val mAdapter by lazy { MultiTypeAdapter() }
    var mRefreshLayout: RefreshLayoutProxy? = null

    abstract fun registerItem(adapter: MultiTypeAdapter)

    override fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        registerItem(mAdapter)
        RecyclerViewUtil.initRecyclerView(recyclerview, mAdapter)
    }


    override fun getRefreshLayout(): SmartRefreshLayout? {
        return refreshLayout
    }


    override fun initData() {
        mRefreshLayout?.autoRefresh()
    }

    override fun initListener() {
        super.initListener()

        //设置刷新回调
        setupRefreshLayout(getRefreshLayout())

        if (mViewModel is BaseListViewModel) {
            val vm = mViewModel as BaseListViewModel

            //完成刷新
            vm.finishRefresh.observe(viewLifecycleOwner, Observer {
                if (it) {
                    mRefreshLayout?.finishRefresh()
                }
            })

            //完成加载更多
            vm.finishLoadMore.observe(viewLifecycleOwner, Observer {
                if (it) {
                    mRefreshLayout?.finishLoadMore()
                }
            })


            //是否能加载更多
            vm.enableLoadMore.observe(viewLifecycleOwner, Observer {
                if (it) {
                    mRefreshLayout?.setEnableLoadMore(it)
                }

            })

            //是否没有更多数据
            vm.noMoreData.observe(viewLifecycleOwner, Observer {
                mRefreshLayout?.setNoMoreData(it)
            })


            //刷新数据
            vm.notifyDataChange.observe(viewLifecycleOwner, Observer {
                if (it > 0) {
                    mAdapter?.notifyItemInserted(it)
                } else {
                    mAdapter?.notifyDataSetChanged()
                }
            })
        }


    }


    /**
     * 子类可以复写刷新类型，默认支持刷新和加载更多
     *
     * @return RefreshType
     */
    protected open fun getRefreshType(): Int {
        return RefreshType.REFRESH_AND_LOAD_MORE
    }


    /**
     * 实例化刷新代理类，设置监听回调
     * @param smartRefreshLayout
     */
    private fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout?) {
        mRefreshLayout = RefreshLayoutProxy(smartRefreshLayout)
        processRefreshType(getRefreshType())
        mRefreshLayout?.setRefreshAndLoadMoreListener(object : OnRefreshAndLoadMoreListener {
            override fun onRefresh(refreshLayout: IRefreshLayout?) {
                loadData(RequestType.REFRESH)
            }

            override fun onLoadMore(refreshLayout: IRefreshLayout?) {
                if (mViewModel is BaseListViewModel) {
                    loadData(RequestType.LOAD_MORE)
                }
            }

        })
    }


    /**
     * 根据刷新类型请求数据,BaseListViewModel做了刷新/加载更多的处理
     * @param requestType
     * @see BaseListViewModel.fetchListData
     */
    private fun loadData(@RefreshType.Val requestType: Int) {
        if (mViewModel is BaseListViewModel) {
            val vm = mViewModel as BaseListViewModel
            vm.fetchListData(requestType)?.observe(viewLifecycleOwner, Observer {

                onLoadDataFinish(it?.data)
            })
        }
    }

    abstract fun onLoadDataFinish(data: Any?)

    /**
     * @param requestType   根据刷新类型设置是否可以刷新或加载更多
     */
    private fun processRefreshType(@RefreshType.Val refreshType: Int) {
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

}