package com.sample.app

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.bsnl.constraint.export.api.IConstraintService
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.onClick
import com.dawn.base.viewmodel.EmptyViewModel
import com.dawn.sample.pkg.feature.view.FirstActivity
import com.sample.app.databinding.AppActivityRunAloneBinding

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   :
 */
class RunAloneActivity : BaseActivity<EmptyViewModel, AppActivityRunAloneBinding>() {

    @Autowired
    @JvmField
    var sampleService: IConstraintService? = null

    override fun initView() {
        binding.tv.onClick ={
            //sampleService?.startBarrierActivity()
            FirstActivity.actionStart(mContext)
        }
    }

    override fun isNeedInjectARouter() =true
}