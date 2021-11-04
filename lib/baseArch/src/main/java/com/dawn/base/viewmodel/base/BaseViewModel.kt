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
    /**
     * 切换页面状态
     */
    protected val _viewState = MutableLiveData(ViewStateWithMsg(msg = null, state = null))
    val viewState: LiveData<ViewStateWithMsg> = _viewState

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








