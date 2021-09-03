package com.bsnl.sample.pkg.feature.viewmodel

import com.bsnl.common.BaseHttpResult
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.sample.pkg.feature.data.entity.CountDownList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/2
 * @desc   :
 */
class CountDownViewModel : BaseListViewModel() {
    override fun getList(): Flow<BaseHttpResult<Any>?> {
        return flow {
            val response = arrayListOf<Long>(
                0,
                20_000,
                30_000,
                1_000,
                20_000,
                30_000,
                0,
                200_000,
                300_000,
                1_000,
                20_000,
                30_000,
                0,
            )
            val bean = CountDownList(response)
            val data = BaseHttpResult<CountDownList>()
            data.data = bean
            data.code = "000000"
            data.msg = "ok"
            emit(data)
        } as Flow<BaseHttpResult<Any>?>
    }
}