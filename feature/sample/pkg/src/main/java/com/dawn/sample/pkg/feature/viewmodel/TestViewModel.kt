package com.dawn.sample.pkg.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dawn.base.viewmodel.base.BaseViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class TestViewModel: BaseViewModel() {

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> = _count



}