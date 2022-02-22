package com.dawn.base.viewmodel.base

import androidx.lifecycle.*
import com.dawn.base.DataResult
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.utils.NetworkUtils
import com.dawn.base.utils.showToast
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

    /** 切换页面状态 */
    val viewState = MutableLiveData(ViewStateWithMsg(msg = null, state = ViewState.STATE_COMPLETED))


    /**
     * 请求数据显示带白色背景的Loading
     */
    fun <T> fetchData(
        flow: Flow<DataResult<T>>,
        viewState: MutableLiveData<ViewStateWithMsg>,
    ): LiveData<T?> = liveData {
        flow.onStart {
            viewState.postValue(
                ViewStateWithMsg(state = ViewState.STATE_LOADING)
            )
        }.catch {
            handleTheCatchException(it.message, viewState)
        }.collectLatest {
            handleCollections(it, viewState)
        }
    }

    /**
     * 只请求数据，不处理页面状态
     */
    @ExperimentalCoroutinesApi
    fun <T> fetchDataWithoutState(
        flow: Flow<DataResult<T>>,
    ): LiveData<T?> = liveData {
        flow.catch {
            handleTheCatchException(it.message, null)
        }.collectLatest {
            handleCollections(it, null)
        }
    }

    /**
     * 提交数据过程中显示透明背景的loading
     */
    fun <T> commitDataWithLoading(
        flow: Flow<DataResult<T>>,
        viewState: MutableLiveData<ViewStateWithMsg>,
    ): LiveData<T?> = liveData {
        flow.onStart {
            viewState.postValue(
                ViewStateWithMsg(state = ViewState.STATE_COMMIT)
            )
        }.catch {
            viewState.postValue(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
            if (!NetworkUtils.isConnected()) {
                "无法联网，请检查网络".showToast()
            } else {
                it.message?.showToast()
            }
        }.collectLatest {
            viewState.postValue(
                ViewStateWithMsg(state = ViewState.STATE_COMPLETED)
            )
            if (it.isSuccessFul) {
                emit(it.data)
            } else {
                it.msg?.showToast()
            }
        }
    }


    private fun handleTheCatchException(
        message: String?,
        viewState: MutableLiveData<ViewStateWithMsg>?
    ) {
        if (!NetworkUtils.isConnected()) {
            "网络出问题了".showToast()
            viewState?.postValue(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
            return
        }
        message?.let {
            viewState?.postValue(
                ViewStateWithMsg(
                    msg = "网络似乎出现了问题",
                    state = ViewState.STATE_ERROR
                )
            )
        }
    }

    private suspend fun <T> LiveDataScope<T?>.handleCollections(
        it: DataResult<T>,
        viewState: MutableLiveData<ViewStateWithMsg>?
    ) {
        if (it.isSuccessFul) {
            viewState?.postValue(
                ViewStateWithMsg(
                    msg = it.msg,
                    state = ViewState.STATE_COMPLETED
                )
            )
            emit(it.data)
        } else {
            it.msg?.let { it.showToast() }
            viewState?.postValue(
                ViewStateWithMsg(
                    msg = it.msg,
                    state = ViewState.STATE_ERROR
                )
            )
        }
    }

}








