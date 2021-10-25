package com.dawn.base.ui.page.iface

import com.dawn.base.BaseHttpResult
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   :
 */
interface IList {

    fun getList(): Flow<BaseHttpResult<Any>?>?
}