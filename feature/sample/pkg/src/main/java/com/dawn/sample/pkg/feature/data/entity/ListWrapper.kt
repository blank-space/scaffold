package com.dawn.sample.pkg.feature.data.entity

/**
 * @author : LeeZhaoXing
 * @date   : 2022/6/4
 * @desc   :
 */

data class ListWrapper<T>(
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var datas: List<T>
)