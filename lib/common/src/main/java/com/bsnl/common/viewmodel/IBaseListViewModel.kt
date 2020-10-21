package com.bsnl.common.viewmodel

import androidx.lifecycle.LiveData
import com.bsnl.common.BaseHttpResult

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   :
 */
interface IBaseListViewModel {

    fun fetchListData(@RequestType.Val requestType: Int):LiveData<BaseHttpResult<Any>?>?

    fun providerData(): MutableList<Any>

}