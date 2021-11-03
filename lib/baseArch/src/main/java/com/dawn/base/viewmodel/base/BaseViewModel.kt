package com.dawn.base.viewmodel.base

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.dawn.base.DataResult
import com.dawn.base.log.L
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
abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    @SuppressLint("StaticFieldLeak")
    var lifecycle: Lifecycle? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        lifecycle = owner.lifecycle
    }

    /**
     * 切换页面状态
     */
    protected val _viewState = MutableLiveData(ViewStateWithMsg(msg = null, state = null))
    val viewState: LiveData<ViewStateWithMsg> = _viewState

    inline fun <T> fetchData(
        flow: Flow<DataResult<T>>,
        isFirstTimeLoad: Boolean = true,
        viewState: MutableLiveData<ViewStateWithMsg>,
        isShowLoadingOnStart: Boolean = true,
        crossinline block: (T) -> Unit,
    ): LiveData<T?> = liveData {
        lifecycle?.let {
            //处于 STARTED 状态时会开始收集数据，并且在 RESUMED 状态时保持收集,最终在进入 STOPPED 状态时结束收集过程。
            flow.flowWithLifecycle(it, Lifecycle.State.CREATED)
                .onStart {
                    if (!isShowLoadingOnStart) return@onStart
                    if (isFirstTimeLoad) {
                        viewState.postValue(
                            ViewStateWithMsg(
                                msg = "",
                                state = ViewState.STATE_LOADING
                            )
                        )
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
                        viewState.postValue(
                            ViewStateWithMsg(
                                msg = it.msg,
                                state = ViewState.STATE_ERROR
                            )
                        )
                    }
                }
        }
    }

    /**
     * 简化获取flow获取liveData的流程
     */
    @ExperimentalCoroutinesApi
    inline fun <T> fetchData(
        flow: Flow<DataResult<T>>,
        crossinline block: (T) -> Unit,
    ): LiveData<T?> = liveData {
        lifecycle?.let {
            flow.flowWithLifecycle(it, Lifecycle.State.CREATED)
                .onStart {
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

    override fun onCleared() {
        super.onCleared()
        L.e("viewModel onCleared")
        lifecycle = null
    }

}