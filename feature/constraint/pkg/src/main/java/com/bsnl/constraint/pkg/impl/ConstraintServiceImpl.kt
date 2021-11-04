package com.bsnl.constraint.pkg.impl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.constraint.export.api.IConstraintService
import com.bsnl.constraint.export.path.ConstraintPath

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
@Route(path = ConstraintPath.S_SERVICE)
class ConstraintServiceImpl : IConstraintService {
    override fun startBarrierActivity() {
      ARouter.getInstance().build(ConstraintPath.A_BARRIER_ACTIVITY).navigation()
    }

    override fun init(context: Context?) {

    }
}