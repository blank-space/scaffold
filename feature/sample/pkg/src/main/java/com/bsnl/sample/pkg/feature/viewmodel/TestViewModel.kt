package com.bsnl.sample.pkg.feature.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.viewmodel.BaseViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class TestViewModel: BaseViewModel() {

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> = _count



}