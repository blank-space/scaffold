package com.bsnl.common.iface

import com.bsnl.common.BaseHttpResult
import kotlinx.coroutines.flow.Flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/19
 * @desc   :
 */
interface IList {

    fun getList(): Flow<BaseHttpResult<Any>?>?
}