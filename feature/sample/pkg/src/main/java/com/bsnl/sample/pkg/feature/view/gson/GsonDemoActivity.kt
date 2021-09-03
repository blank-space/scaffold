package com.bsnl.sample.pkg.feature.view.gson

import android.content.Context
import com.bsnl.common.page.base.BaseBindingActivity
import com.bsnl.common.utils.startActivity
import com.bsnl.common.viewmodel.StubViewModel
import com.bsnl.sample.pkg.databinding.FeatureSamplePkgActivityGsonBinding
import com.bsnl.sample.pkg.feature.view.FirstActivity

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/2
 * @desc   :
 */
class GsonDemoActivity : BaseBindingActivity<StubViewModel,FeatureSamplePkgActivityGsonBinding>() {

    companion object {
        fun actionStart(context: Context) {
            startActivity<GsonDemoActivity>(context)
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }
}