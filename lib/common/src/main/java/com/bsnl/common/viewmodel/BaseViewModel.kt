package com.bsnl.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   :
 */
abstract class BaseViewModel : ViewModel() {
    /**
     * 切换页面状态
     */
    val viewState: MutableLiveData<ViewStateWithMsg> = MutableLiveData(ViewStateWithMsg(msg = "",state = ViewState.STATE_IDLE))


}