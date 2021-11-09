package com.dawn.mock.constraint

import android.content.Context
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.bsnl.constraint.export.api.IConstraintService
import com.dawn.mock.MockPath

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
@Route(path = MockPath.S_SERVICE)
class ConstraintMockApi :IConstraintService {

    private var ctx :Context?=null
    override fun startBarrierActivity() {
        ctx?.let {
            Toast.makeText(it,"startBarrierActivity",Toast.LENGTH_SHORT).show()
        }

    }

    override fun init(context: Context?) {
        ctx = context
    }

}