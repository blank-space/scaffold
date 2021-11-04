package com.dawn.sample.pkg.bebug

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.bsnl.constraint.export.api.IConstraintService
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.onClick
import com.dawn.base.viewmodel.EmptyViewModel
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityLocationBinding
import com.dawn.sample.pkg.databinding.PkgActiivityRunAloneBinding

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
class RunAloneActivity : BaseActivity<EmptyViewModel,PkgActiivityRunAloneBinding>() {

    @Autowired
    @JvmField
    var sampleService: IConstraintService? = null
    override fun initView() {
        binding.tv.onClick ={
            sampleService?.startBarrierActivity()
        }
    }

    override fun isNeedInjectARouter() =true
}