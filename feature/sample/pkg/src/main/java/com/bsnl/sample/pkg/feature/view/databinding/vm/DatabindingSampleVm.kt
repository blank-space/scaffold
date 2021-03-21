package com.bsnl.sample.pkg.feature.view.databinding.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.viewmodel.BaseViewModel
import com.bsnl.sample.pkg.feature.view.databinding.GoodsBean

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/10
 * @desc   :
 */
class DatabindingSampleVm : BaseViewModel(){
   // dataBinding.goods = GoodsBean( 1L)

    val goods= MutableLiveData(GoodsBean( 1L))
}