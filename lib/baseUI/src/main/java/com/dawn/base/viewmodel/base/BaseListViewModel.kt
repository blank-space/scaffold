package com.dawn.base.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dawn.base.utils.NetworkUtils
import com.dawn.base.utils.showToast
import com.dawn.base.DataResult
import com.dawn.base.constant.DEFAULT_PAGE_SIZE
import com.dawn.base.constant.DEFAULT_START_PAGE_INDEX
import com.dawn.base.ui.page.iface.IList
import com.dawn.base.ui.page.iface.RefreshType
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.viewmodel.iface.IBaseList
import com.dawn.base.viewmodel.iface.IBaseListViewModel
import com.dawn.base.viewmodel.iface.RequestType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   : 通用列表基础ViewModel
 */
abstract class BaseListViewModel : BaseViewModel(), IList, IBaseListViewModel {

    @RefreshType.Val
    var mRequestType = 0
    protected open var pageNo = DEFAULT_START_PAGE_INDEX
    protected open var pageSize: Int = DEFAULT_PAGE_SIZE
    protected var mData: MutableList<Any> = mutableListOf()


    /**
     * 结束刷新动作
     */
    private val _finishRefresh = MutableLiveData(false)
    val finishRefresh: LiveData<Boolean> = _finishRefresh

    /**
     * 结束加载更多动作
     */
    private val _finishLoadMore = MutableLiveData(false)
    val finishLoadMore: LiveData<Boolean> = _finishLoadMore

    /**
     * adapter更新数据
     */
    private val _notifyDataChange = MutableLiveData(0)
    val notifyDataChange: LiveData<Int> = _notifyDataChange

    /**
     * 是否可以加载更多
     */
    private val _enableLoadMore = MutableLiveData(true)
    val enableLoadMore: LiveData<Boolean> = _enableLoadMore

    /**
     * 没有更多数据
     */
    private val _noMoreData = MutableLiveData(false)
    val noMoreData: LiveData<Boolean> = _noMoreData

    /**
     * 请求入口
     */
    @ExperimentalCoroutinesApi
    override fun fetchListData(requestType: Int): LiveData<DataResult<Any>?>? {
        mRequestType = requestType
        when (requestType) {
            RequestType.INIT -> processPreInitData()
            RequestType.REFRESH -> pageNo = DEFAULT_START_PAGE_INDEX
            RequestType.LOAD_MORE -> { }
        }

        return liveData {
            getList()?.catch {
                processError("网络似乎出现了问题")
            }?.collectLatest {
                    if (it?.isSuccessFul!!) {
                        processData(it)
                        emit(it as DataResult<Any>?)
                    } else {
                        processError(it.msg)
                    }
                }
        }
    }

    /**
     * 处理错误
     */
    private fun processError(errMsg: String?) {
        when (mRequestType) {
            RequestType.REFRESH -> {
                _finishRefresh.postValue(true)
            }
            RequestType.LOAD_MORE -> {
                pageNo--
                if (pageNo < 0) pageNo = DEFAULT_START_PAGE_INDEX
                _finishLoadMore.postValue(true)
            }
        }
        processInitDataError(errMsg)
    }


    /**
     * 处理数据
     */
    protected open fun processData(t: DataResult<Any>?) {
        if (t == null) {
            setState(value = ViewState.STATE_ERROR)
            return
        }
        when (mRequestType) {
            RequestType.INIT -> {
                setState(value = ViewState.STATE_COMPLETED)
                initView(t, mRequestType)
            }
            RequestType.REFRESH -> {
                _finishRefresh.postValue(true)
                initView(t, mRequestType)
            }
            RequestType.LOAD_MORE -> {
                _finishLoadMore.postValue(true)
                processLoadMoreData(t)
            }
            RequestType.SILENT_REFRESH -> initView(t, mRequestType)
        }
    }

    /**
     * 初始化页面数据（子类可重写）
     */
    protected open fun initView(
        listResponseBean: DataResult<Any>?,
        @RequestType.Val requestType: Int
    ) {
        if (listResponseBean?.data is IBaseList) {
            val data: IBaseList = listResponseBean.data as IBaseList
            if (!data.getDataList().isNullOrEmpty()) {
                mData.clear()
                mData.addAll(data.getDataList()!!)
                processInitLoadMoreState(data)
                pageNo++
                _notifyDataChange.postValue(0)
            } else {
                processEmptyState()
            }
        } else {
            throw  RuntimeException("列表数据必须实现IBaseList接口")
        }
    }

    /**
     * Desc:设置初始化页面或刷新页面时是否可以加载更多，如果页面默认没有加载更多，
     * 可重写，默认空实现
     * @param baseList
     */
    protected open fun processInitLoadMoreState(baseList: IBaseList?) {
        if (isLoadMoreNoData(baseList)) {
            finishLoadMoreWithNoMoreData()
        } else {
            _enableLoadMore.postValue(true)
        }
    }

    /**
     * 处理没有更多数据
     */
    protected open fun finishLoadMoreWithNoMoreData() {
        _enableLoadMore.postValue(false)
        _noMoreData.postValue(true)

    }


    /**
     * Desc:加载更多没有后续数据
     *
     * @param baseList
     * @return boolean true:没有后续数据
     */
    protected open fun isLoadMoreNoData(baseList: IBaseList?): Boolean {
        if (baseList?.getTotals()!! > 0) {
            return mData.size >= baseList.getTotals()
        } else {
            return judgeLoadMoreNoDataByPageSize(baseList)
        }
    }

    /**
     * Desc:处理加载更多的数据（子类可重写）
     *
     * @param listResponseBean
     */
    protected open fun processLoadMoreData(listResponseBean: DataResult<Any>?) {
        if (listResponseBean?.data is IBaseList) {
            val data: IBaseList = listResponseBean?.getData() as IBaseList
            val insertP = mData.size
            if (!data.getDataList().isNullOrEmpty()) {
                mData.addAll(data.getDataList()!!)
                if (isLoadMoreNoData(data)) {
                    finishLoadMoreWithNoMoreData()
                }
                pageNo++
            } else {
                finishLoadMoreWithNoMoreData()
            }
            _notifyDataChange.postValue(insertP)
        } else {
            throw  RuntimeException("列表数据必须实现IBaseList接口")
        }
    }

    /**
     *  通过pageSize判断更多没有后续数据
     */
    private fun judgeLoadMoreNoDataByPageSize(baseList: IBaseList?): Boolean {
        return baseList?.getDataList()?.size!! < pageSize
    }

    /**
     * 初始化页面数据获取失败后的处理(子类可重写)
     */
    protected open fun processInitDataError(errMsg: String?) {
        if (mData.size == 0) {
            if (errMsg.isNullOrBlank()) {
                setState("网络似乎出现了问题", ViewState.STATE_NETWORK_ERROR)
            } else {
                setState(errMsg, ViewState.STATE_ERROR)
            }
            return
        }
        if (!NetworkUtils.isConnected()) {
            "当前无网路连接，请检查网络".showToast()
            setState("网络似乎出现了问题", ViewState.STATE_NETWORK_ERROR)
        }
    }


    /**
     * 初始化数据前的逻辑处理(子类可重写)
     */
    protected open fun processPreInitData() {
        pageNo = DEFAULT_START_PAGE_INDEX
        setState("", ViewState.STATE_LOADING)
    }

    private fun setState(msg: String? = "", value: ViewState) {
        _viewState.postValue(ViewStateWithMsg(msg = msg, state = value))
    }

    override fun providerData(): MutableList<Any> {
        return mData
    }

    /**
     * 处理空状态
     */
    protected open fun processEmptyState() {
        setState("空空如也～", ViewState.STATE_EMPTY)
    }

}