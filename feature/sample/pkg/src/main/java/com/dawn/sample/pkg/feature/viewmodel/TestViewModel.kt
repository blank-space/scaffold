package com.dawn.sample.pkg.feature.viewmodel

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
 * @desc   : savedStateHandle
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