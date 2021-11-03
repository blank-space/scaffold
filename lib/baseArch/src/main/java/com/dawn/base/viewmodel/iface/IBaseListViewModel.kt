package com.dawn.base.viewmodel.iface

import androidx.lifecycle.LiveData
import com.dawn.base.DataResult

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   :
 */
interface IBaseListViewModel {

    fun fetchListData(@RequestType.Val requestType: Int):LiveData<DataResult<out Any>?>?

    fun providerData(): MutableList<Any>

}