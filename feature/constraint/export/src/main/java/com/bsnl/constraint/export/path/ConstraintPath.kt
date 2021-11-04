package com.bsnl.constraint.export.path

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
interface ConstraintPath {
    companion object {
        private const val GROUP = "/ConstraintGroup/"
        const val S_SERVICE = GROUP + "ConstraintService"
        const val A_BARRIER_ACTIVITY = GROUP + "BarrierActivity"
    }
}