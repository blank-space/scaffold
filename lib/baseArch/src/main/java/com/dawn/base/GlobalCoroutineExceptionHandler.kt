package com.dawn.base

import com.dawn.base.log.L
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * @author : LeeZhaoXing
 * @date   : 2022/2/9
 * @desc   : 全局的coroutine异常捕获
 */
class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        L.e("Global Coroutine exception : $exception")
    }
}