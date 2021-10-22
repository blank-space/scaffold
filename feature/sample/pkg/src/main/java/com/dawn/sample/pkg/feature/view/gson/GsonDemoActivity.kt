package com.dawn.sample.pkg.feature.view.gson

import android.content.Context
import com.dawn.base.ui.page.base.BaseBindingActivity
import com.dawn.base.utils.startActivity
import com.dawn.base.viewmodel.StubViewModel
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityGsonBinding

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