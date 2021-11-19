package com.dawn.sample.pkg.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dawn.base.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */

@HiltViewModel
class TestViewModel @Inject constructor(): BaseViewModel() {

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> = _count

   // val plantId: String = savedStateHandle.get<String>(PLANT_ID_SAVED_STATE_KEY)!!

    companion object {
        private const val PLANT_ID_SAVED_STATE_KEY = "plantId"
    }

}