package com.bsnl.sample.pkg.feature.mapper

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/6
 * @desc   :  使用 Data Mapper 分离数据源的 Model 和 页面显示的 Model，
 * 不要因为数据源的增加、修改或者删除，导致上层页面也要跟着一起修改，换句话说使用 Data Mapper 做一个中间转换
 */
interface Mapper<I, O> {
    fun map(input: I): O
}