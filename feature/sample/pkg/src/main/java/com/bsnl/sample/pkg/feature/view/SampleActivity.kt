package com.bsnl.sample.pkg.feature.view

import android.content.Context
import com.bsnl.common.dataBinding.BaseListDataBindingActivity
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.ui.titlebar.ToolbarTitleView
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.BR
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.SampleViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class SampleActivity : BaseListDataBindingActivity<SampleViewModel>() {

    companion object {
        fun actionStart(context: Context) {
            startActivity<SampleActivity>(context)
        }
    }

    override fun registerItem(adapter: MultiTypeAdapter) {
        adapter.register(PokemonItemViewBinder())

    }


    override fun initView() {
        super.initView()
        mTitleView = findViewById<ToolbarTitleView>(R.id.toolbar)
        initTitle(TAG)
    }

    override fun getLayoutId() = R.layout.feature_sample_pkg_activity_sample

    override fun initBindingConfig(layoutId: Int): DataBindingConfig {
        return DataBindingConfig(layoutId, BR.viewModel, mViewModel)
    }

    override fun initViewModel(): SampleViewModel = getVm()
}