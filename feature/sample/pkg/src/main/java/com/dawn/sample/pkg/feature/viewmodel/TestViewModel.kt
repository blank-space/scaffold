package com.dawn.sample.pkg.feature.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.dawn.base.log.L
import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.sample.pkg.feature.constant.BUNDLE_INDEX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   : savedStateHandle：单独使用ViewModel无法恢复，被系统回收时（可使用adb命令来模拟），不会执行onDestroy方法，需要ViewModel搭配SavedStateHandle使用。
 * SavedStateHandle内部使用了activity的异常机制，在onSaveInstanceState方法中把数据保存在Bundle中，然后在onCreate去恢复数据，把数据放到新创建viewModel中。
 */
@HiltViewModel
class TestViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> = _count


    private fun getMutableLiveData(): MutableLiveData<Int> {
        val index = savedStateHandle.get<Int>(BUNDLE_INDEX)?:-1
        Log.d("@@","index : $index")
        if (!savedStateHandle.contains(BUNDLE_INDEX)) {
            this.savedStateHandle.set(BUNDLE_INDEX, 0)
        }
        return this.savedStateHandle.getLiveData(BUNDLE_INDEX)
    }

    fun add() {
        getMutableLiveData().value = getMutableLiveData().value!! + 1
    }

    fun getCount() = getMutableLiveData().value
}