package com.bsnl.common.dataBinding

import androidx.recyclerview.widget.RecyclerView
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.page.delegate.IListViewDelegate
import com.bsnl.common.page.delegate.ListViewDelegateImpl
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.BaseViewModel
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.lib_common_refreshlayout.*


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 基础列表Activity ， 子类布局文件必须include lib_common_refreshlayout.xml
 *
 */
abstract class ListDataBindingActivity<T : BaseViewModel> : DataBindingActivity<T>() {

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
        mListViewDelegate?.initRecyclerView(recyclerview)

    }

    fun getRecyclerView(): RecyclerView? = mListViewDelegate?.getRecyclerView()


    fun getAdapter(): MultiTypeAdapter? = mListViewDelegate?.getAdapter()


    override fun getRefreshLayout(): SmartRefreshLayout? {
        return refreshLayout
    }


    override fun initData() {
        mListViewDelegate?.getRefreshLayoutProxy()?.autoRefresh()
    }

    override fun initListener() {
        super.initListener()
        //设置刷新、加载更多
        mListViewDelegate?.setupRefreshLayout(getRefreshLayout())
        //监听LiveData的通知
        mListViewDelegate?.observeLiveDataCallback()
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


    /**
     * 提供给外部做一些额外的处理
     */
    protected open fun onGetDataFinish(data: Any?) {}


}