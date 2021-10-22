package com.dawn.base.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.dawn.base.log.L
import com.dawn.base.BaseHttpResult
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/20
 * @desc   :
 */
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
            viewState.postValue(
                ViewStateWithMsg(
                    msg = it.msg,
                    state = ViewState.STATE_COMPLETED
                )
            )
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
    }.catch {
        if (!NetworkUtils.isConnected()) {
            "网络出问题了".showToast()
        } else {
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
    if (!NetworkUtils.isConnected()) {
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