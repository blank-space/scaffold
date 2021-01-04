package com.bsnl.constraint.pkg.impl

import android.content.Context
import com.bsnl.base.utils.ApiUtils
import com.bsnl.constraint.export.api.ConstrainApi
import com.bsnl.constraint.pkg.feature.ConstraintDemoActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2021/1/4
 * @desc   :
 */
@ApiUtils.Api
class ConstrainApiImpl : ConstrainApi() {
    override fun startSampleActivity(context: Context) {
        ConstraintDemoActivity.startAction(context)
    }
}