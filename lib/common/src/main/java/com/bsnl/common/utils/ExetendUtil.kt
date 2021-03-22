package com.bsnl.common.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.bsnl.base.dsl.dp
import com.bsnl.base.utils.showToast
import com.bsnl.common.BaseHttpResult
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   : 高阶函数、内联函数
 */
/**
 * 传参启动Activity
 */
inline fun <reified T> startActivity(context: Context, block: Intent.() -> Unit) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivity(intent)
}


/**
 * startActivityForResult启动Activity
 */
inline fun <reified T> startActivityForResult(
    context: FragmentActivity,
    block: Intent.() -> Unit,
    requestCode: Int
) {
    val intent = Intent(context, T::class.java)
    intent.block()
    context.startActivityForResult(intent, requestCode)
}

/**
 * 启动Activity
 */
inline fun <reified T> startActivity(context: Context) {
    val intent = Intent(context, T::class.java)
    context.startActivity(intent)
}

/**
 * 创建Fragment
 */
inline fun <reified T : Fragment> newFrgInstance(block: Bundle.() -> Unit): T {
    val clazz = T::class.java.newInstance()
    val args = Bundle().apply(block)
    clazz.arguments = args
    return clazz
}

inline fun <T, R> T.doWithTry(block: (T) -> R) {
    try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

/**
 * 简化获取flow获取liveData的流程
 */
@ExperimentalCoroutinesApi
inline fun <T> fetchLiveData(
    flow: Flow<BaseHttpResult<T?>?>,
    isFirstTimeLoad: Boolean = true,
    viewState: MutableLiveData<ViewStateWithMsg>,
    crossinline block: (T) -> Unit
): LiveData<T?> = liveData {
    flow.onStart {
        if (isFirstTimeLoad) {
            viewState.postValue(ViewStateWithMsg(msg = "", state = ViewState.STATE_LOADING))
        } else {
            viewState.postValue(ViewStateWithMsg(msg = "", state = ViewState.STATE_SHOW_LOADING_DIALOG))
        }
    }.catch {
        viewState.postValue(ViewStateWithMsg(it.message.toString(), state = ViewState.STATE_ERROR))
    }.collectLatest {
        if (it?.isSuccessFul()!! && it.data != null) {
            viewState.postValue(ViewStateWithMsg(msg = it.msg, state = ViewState.STATE_COMPLETED))
            block(it.data!!)
            emit(it.data)
        } else {
            it.msg.showToast()
            viewState.postValue(ViewStateWithMsg(msg = it.msg, state = ViewState.STATE_ERROR))
        }

    }
}


@ExperimentalCoroutinesApi
inline fun <T> createFlow(crossinline block: () -> BaseHttpResult<T?>?): Flow<BaseHttpResult<T?>?> {
    return flow { emit(block()) }.flowOn(Dispatchers.IO)
}

/**
 * 共享Activity
 */
inline fun <reified T : ViewModel> Fragment.getVm(): T {
    return ViewModelProvider(this.activity as FragmentActivity).get(T::class.java)
}

/**
 * 独立作用域
 */
inline fun <reified T : ViewModel> Fragment.getSelfVm(): T {
    return ViewModelProvider(this).get(T::class.java)
}


inline fun <reified T : ViewModel> FragmentActivity.getVm(): T {
    return ViewModelProvider(this).get(T::class.java)
}


/*fun <T : ViewModel> Fragment.getVm(clazz: Class<T>): T {
    return ViewModelProvider(this).get(clazz)
}

fun <T : ViewModel> FragmentActivity.getVm(clazz: Class<T>): T {
    return ViewModelProvider(this).get(clazz)
}*/

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp get() = this.toFloat().dp



