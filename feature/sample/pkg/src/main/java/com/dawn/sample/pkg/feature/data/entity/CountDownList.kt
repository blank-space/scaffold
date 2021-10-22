package com.dawn.sample.pkg.feature.data.entity

import com.dawn.base.viewmodel.iface.IBaseList

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/2
 * @desc   :
 */
data class CountDownList(val list: MutableList<Long>) : IBaseList {
    override fun getDataList(): MutableList<Long> = list

    override fun getTotals(): Int = list.size
}