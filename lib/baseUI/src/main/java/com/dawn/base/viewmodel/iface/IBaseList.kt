package com.dawn.base.viewmodel.iface


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   : 通用列表接口
 */
interface IBaseList {


    /**
     *获取数据
     */
    fun getDataList(): List<Any>?



    /**
     *获取数据总条数
     */
    fun getTotals(): Int


}