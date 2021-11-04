package com.dawn.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.dawn.base.BaseApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   : 高阶函数、内联函数
 */
inline fun <T, R> T.doWithTry(block: (T) -> R) {
    try {
        block(this)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )


@ExperimentalCoroutinesApi
fun <T> simpleSlow(block: suspend FlowCollector<T>.() -> Unit): Flow<T> = flow(block).flowOn(
    Dispatchers.IO
)

/** 替代 Handler.post/postDelayed */
fun LifecycleOwner.postDelayedOnLifecycle(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job = lifecycleScope.launch(dispatcher) {
    delay(duration)
    block()
}


/** 替代 View.post/postDelayed */
fun View.postDelayedOnLifecycle(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block:() -> Unit
) : Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycleScope.launch(dispatcher) {
        delay(duration)
        block()
    }
}


