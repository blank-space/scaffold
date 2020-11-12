package com.bsnl.sample.pkg.feature.view

import android.content.Context
import android.view.View
import com.bsnl.base.utils.showToast
import com.bsnl.common.dataBinding.ListDataBindingActivity
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.iface.OnItemClickListener
import com.bsnl.common.ui.titlebar.ToolbarTitleView
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.common.viewmodel.RequestType
import com.bsnl.sample.pkg.BR
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.bsnl.sample.pkg.feature.itemViewBinder.StringItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.SampleViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class SampleActivity : ListDataBindingActivity<SampleViewModel>() {

    companion object {
        fun actionStart(context: Context) {
            startActivity<SampleActivity>(context)
        }
    }

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.apply {
            register(StringItemViewBinder())
            register(PokemonItemViewBinder())
            setHasStableIds(true)
        }
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

    override fun initListener() {
        super.initListener()
        RecyclerViewUtil.setOnItemClickListener(getRecyclerView(), object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                FirstActivity.actionStart(this@SampleActivity)
            }

            override fun onChildClick(v: View, position: Int) {
                v.id.toString().showToast()
                FirstActivity.actionStart(this@SampleActivity)
            }
        }, R.id.name, R.id.avator)
    }

    override fun onGetDataFinish(data: Any?) {
        super.onGetDataFinish(data)
        if (mViewModel.mRequestType == RequestType.REFRESH) {
            mViewModel.providerData().add(0, "MultiType")
        }
    }
}