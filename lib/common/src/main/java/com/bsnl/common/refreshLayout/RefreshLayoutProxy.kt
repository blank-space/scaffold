package com.bsnl.common.refreshLayout


import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.OnRefreshAndLoadMoreListener
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   : 刷新控件代理类(主要是考虑后期升级方法和实现可能会变更或刷新控件替换)
 */
class RefreshLayoutProxy(val mRefreshLayout: SmartRefreshLayout? = null, var listener: OnRefreshAndLoadMoreListener? = null) : IRefreshLayout {

    init {
        mRefreshLayout?.setEnableOverScrollDrag(false) //禁止越界拖动（1.0.4以上版本）
        mRefreshLayout?.setEnableOverScrollBounce(false) //关闭越界回弹功能
        mRefreshLayout?.setEnableNestedScroll(false)
        mRefreshLayout?.setEnableFooterTranslationContent(true)
        mRefreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                listener?.onLoadMore(this@RefreshLayoutProxy)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                listener?.onRefresh(this@RefreshLayoutProxy)
            }
        })
        mRefreshLayout?.setRefreshHeader(ClassicsHeader(mRefreshLayout?.context))
        mRefreshLayout?.setRefreshFooter(ClassicsFooter(mRefreshLayout?.context))

    }


    override fun setRefreshAndLoadMoreListener(lisn: OnRefreshAndLoadMoreListener?): IRefreshLayout? {
        this.listener = lisn

        return this
    }

    override fun setEnableRefresh(enable: Boolean): IRefreshLayout? {
        mRefreshLayout?.setEnableRefresh(enable)
        return this
    }

    override fun setEnableLoadMore(enable: Boolean): IRefreshLayout? {
        mRefreshLayout?.setEnableLoadMore(enable)

        return this
    }

    override fun finishRefresh() {
        mRefreshLayout?.finishRefresh()
    }

    override fun finishLoadMore() {
        mRefreshLayout?.finishLoadMore()
    }

    override fun autoRefresh() {
        mRefreshLayout?.autoRefresh()
    }

    override fun setNoMoreData(boolean: Boolean) {
        mRefreshLayout?.setNoMoreData(boolean)
    }


}