package com.dawn.base

import com.dawn.base.viewmodel.iface.IBaseList

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
class BaseListBean<T>(var results: List<T>, var total: Int = results.size) : IBaseList {

    override fun getDataList() = results as List<Any>

    override fun getTotals() = total


}