package com.bsnl.common.iface

import androidx.annotation.NonNull

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
interface OnRefreshAndLoadMoreListener {

    fun onRefresh(@NonNull refreshLayout: IRefreshLayout?)
    fun onLoadMore(@NonNull refreshLayout: IRefreshLayout?)


}