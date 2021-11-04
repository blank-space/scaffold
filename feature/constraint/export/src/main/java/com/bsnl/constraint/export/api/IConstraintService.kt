package com.bsnl.constraint.export.api

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
interface IConstraintService : IProvider {
     fun startBarrierActivity()
}

class ConstraintParam(var name: String)

class ConstraintResult(var name: String)