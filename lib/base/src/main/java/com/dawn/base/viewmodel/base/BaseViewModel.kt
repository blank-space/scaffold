package com.dawn.base.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dawn.base.ui.page.iface.ViewStateWithMsg


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



}