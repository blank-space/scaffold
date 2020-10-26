package com.bsnl.sample.pkg.feature.view

import android.content.Context
import com.bsnl.common.dataBinding.DataBindingActivity
import com.bsnl.common.dataBinding.DataBindingConfig
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.feature_sample_pkg_fragment_first.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class FirstActivity : DataBindingActivity<TestViewModel>() {
    private var isShow = true
    private lateinit var firstFragment: FirstFragment
    companion object{
        fun actionStart(context: Context){
            startActivity<FirstActivity>(context)
        }
    }
    override fun initView() {
        firstFragment = FirstFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, firstFragment, "first")
            commit()
        }

    }

    override fun getLayoutId(): Int = R.layout.feature_sample_pkg_activity_main

    override fun initBindingConfig(layoutId: Int): DataBindingConfig? {
        return null
    }

    override fun initViewModel(): TestViewModel = getVm()

    override fun initData() {
    }


    override fun initListener() {
        super.initListener()
        tv.setOnClickListener {
            if(isShow){
                tv.setText("显示")
                firstFragment.hideSelf(R.anim.lib_common_no_anim,R.anim.lib_common_push_bottom_out)
            }else{
                tv.setText("隐藏")
                firstFragment.showSelf(R.anim.lib_common_push_bottom_in,R.anim.lib_common_no_anim)
            }
            isShow = !isShow

        }
    }
}