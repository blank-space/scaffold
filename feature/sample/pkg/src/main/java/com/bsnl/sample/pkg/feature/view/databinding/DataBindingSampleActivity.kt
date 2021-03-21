package com.bsnl.sample.pkg.feature.view.databinding

import android.content.Context
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.databinding.DataBindingActivity
import com.bsnl.databinding.DataBindingConfig
import com.bsnl.sample.pkg.BR
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.view.databinding.vm.DatabindingSampleVm

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/10
 * @desc   : 自定义View双向绑定
 */
class DataBindingSampleActivity : DataBindingActivity<DatabindingSampleVm>() {

    companion object {
        fun startAction(context: Context) {
            startActivity<DataBindingSampleActivity>(context)
        }
    }

    override fun initView() {
        initTitle(TAG)
    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_activity_databinding

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? {
        return DataBindingConfig(layoutId, BR.viewmodel, mViewModel)
    }

    override fun initViewModel(): DatabindingSampleVm = getVm()

    override fun initData() {

    }

}