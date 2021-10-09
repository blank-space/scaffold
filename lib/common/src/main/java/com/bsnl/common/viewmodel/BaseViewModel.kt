package com.bsnl.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bsnl.base.log.L
import com.bsnl.base.utils.showToast
import com.bsnl.common.BaseHttpResult
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.utils.NetworkUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   :
 */
abstract class BaseViewModel : ViewModel() {
    /**
     * 切换页面状态
     */
    protected val _viewState = MutableLiveData(ViewStateWithMsg(msg = null, state = null))
    val viewState: LiveData<ViewStateWithMsg> = _viewState

    inline fun <T> fetchData(
        flow: Flow<BaseHttpResult<T>>,
        isFirstTimeLoad: Boolean = true,
        viewState: MutableLiveData<ViewStateWithMsg>,
        isShowLoadingOnStart: Boolean = true,
        crossinline block: (T) -> Unit,
    ): LiveData<T?> = liveData {
        flow.onStart {
            if (!isShowLoadingOnStart) return@onStart
            if (isFirstTimeLoad) {
                viewState.postValue(ViewStateWithMsg(msg = "", state = ViewState.STATE_LOADING))
            } else {
                viewState.postValue(
                    ViewStateWithMsg(
                        state = ViewState.STATE_SHOW_LOADING_DIALOG
                    )
                )
            }
        }.catch {
            handleTheCatchException(it.message, viewState)
        }.collectLatest {
            L.i("OkHttp >>>", it.toString())
            if (it.isSuccessFul) {
                viewState.postValue(ViewStateWithMsg(msg = it.msg, state = ViewState.STATE_COMPLETED))
                block(it.data)
                emit(it.data)
            } else {
                it.msg.showToast()
                viewState.postValue(ViewStateWithMsg(msg = it.msg, state = ViewState.STATE_ERROR))
            }
        }
    }
    /**
     * 简化获取flow获取liveData的流程
     */
    @ExperimentalCoroutinesApi
    inline fun <T> fetchData(
        flow: Flow<BaseHttpResult<T>>,
        crossinline block: (T) -> Unit,
    ): LiveData<T?> = liveData {
        flow.onStart {
            L.i("OkHttp >>>", "onStart")
        }.catch {
            if(!NetworkUtils.isConnected()){
                "网络出问题了".showToast()
            }else{
                L.e("error:${it.message}")
                it.message?.showToast()
            }
        }.collectLatest {
            L.i("OkHttp >>>", it.toString())
            if (it.isSuccessFul) {
                block(it.data)
                emit(it.data)
            } else {
                it.msg.showToast()
            }
        }
    }

    fun handleTheCatchException(message: String?, viewState: MutableLiveData<ViewStateWithMsg>) {
        if(!NetworkUtils.isConnected()){
            "网络出问题了".showToast()
            viewState.postValue(ViewStateWithMsg(msg = "", state = ViewState.STATE_COMPLETED))
            return
        }
        message?.let {
            viewState.postValue(
                ViewStateWithMsg(
                    msg = "网络似乎出现了问题",
                    state = ViewState.STATE_ERROR
                )
            )
        }
    }

}