package com.dawn.mock.constraint

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.bsnl.constraint.export.api.IConstraintService
import com.dawn.base.utils.showToast
import com.dawn.mock.MockPath

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
@Route(path = MockPath.S_SERVICE)
class ConstraintMockApi :IConstraintService {

    override fun startBarrierActivity() {
        "mock startBarrierActivity".showToast()
    }

    override fun init(context: Context?) {
    }

}