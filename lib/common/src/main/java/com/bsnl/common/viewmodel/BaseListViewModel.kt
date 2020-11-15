package com.bsnl.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.bsnl.base.BaseApp
import com.bsnl.base.utils.showToast
import com.bsnl.common.BaseHttpResult
import com.bsnl.common.iface.IList
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.constant.DEFAULT_PAGE_SIZE
import com.bsnl.common.constant.DEFAULT_START_PAGE_INDEX
import com.bsnl.common.utils.NetworkUtils
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
    protected var pageNo = DEFAULT_START_PAGE_INDEX
    protected var pageSize: Int = DEFAULT_PAGE_SIZE
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
    override fun fetchListData(requestType: Int): LiveData<BaseHttpResult<Any>?>? {
        mRequestType = requestType
        when (requestType) {
            RequestType.INIT -> processPreInitData()
            RequestType.REFRESH -> pageNo = 1
            RequestType.LOAD_MORE -> {
            }
        }


        return liveData {
            getList()?.catch {
                processError(it.message, !NetworkUtils.isConnected(BaseApp.application))
            }
                ?.collectLatest {
                    if (it?.isSuccessFul!!) {
                        processData(it)
                        emit(it)
                    } else {
                        processError(
                            it.msg,
                            !NetworkUtils.isConnected(BaseApp.application)
                        )
                    }
                }
        }
    }

    /**
     * 处理错误
     */
    private fun processError(errMsg: String?, isNetError: Boolean) {
        when (mRequestType) {
            RequestType.INIT -> processInitDataError(errMsg)
            RequestType.REFRESH -> {
                if (isNetError) {
                    "网络连接失败，请检查您的网络...".showToast()
                } else {
                    errMsg?.showToast()
                }
                _finishRefresh.postValue(true)
            }
            RequestType.LOAD_MORE -> {
                pageNo--
                if (isNetError) {
                    "网络连接失败，请检查您的网络...".showToast()
                } else {
                    errMsg?.showToast()
                }
                _finishLoadMore.postValue(true)
            }
        }
    }


    /**
     * 处理数据
     */
    protected open fun processData(t: BaseHttpResult<Any>?) {
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
        }
    }

    /**
     * 初始化页面数据（子类可重写）
     */
    protected open fun initView(
        listResponseBean: BaseHttpResult<Any>?,
        @RequestType.Val requestType: Int
    ) {
        if (listResponseBean?.getData() is IBaseList) {
            val data: IBaseList = listResponseBean?.getData() as IBaseList
            if (data != null && !data.getDataList().isNullOrEmpty()) {
                mData?.clear()
                mData?.addAll(data.getDataList()!!)
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
            return mData?.size!! >= baseList.getTotals()
        } else {
            return judgeLoadMoreNoDataByPageSize(baseList)
        }
    }

    /**
     * Desc:处理加载更多的数据（子类可重写）
     *
     * @param listResponseBean
     */
    protected open fun processLoadMoreData(listResponseBean: BaseHttpResult<Any>?) {
        if (listResponseBean?.data is IBaseList) {
            val data: IBaseList = listResponseBean?.getData() as IBaseList
            val insertP = mData!!.size
            if (data != null && !data.getDataList().isNullOrEmpty()) {
                mData!!.addAll(data.getDataList()!!)
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
     *  通过pageSize加载更多没有后续数据
     */
    private fun judgeLoadMoreNoDataByPageSize(baseList: IBaseList?): Boolean {
        return baseList?.getDataList()?.size!! < pageSize
    }

    /**
     * 初始化页面数据获取失败后的处理(子类可重写)
     */
    protected open fun processInitDataError(errMsg: String?) {
        setState(errMsg, ViewState.STATE_ERROR)
    }


    /**
     * 初始化数据前的逻辑处理(子类可重写)
     */
    protected open fun processPreInitData() {
        pageNo = 1
        setState("", ViewState.STATE_LOADING)
    }

    private fun setState(msg: String? = "", value: ViewState) {
        _viewState.postValue(ViewStateWithMsg(msg, value))
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