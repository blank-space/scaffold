package com.dawn.sample.pkg.feature.view

import android.content.Context
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.ui.page.base.BaseBindingActivity
import com.dawn.base.utils.startActivity
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityMainBinding
import com.dawn.sample.pkg.feature.viewmodel.TestViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 */
class FirstActivity : BaseBindingActivity<TestViewModel,FeatureSamplePkgActivityMainBinding>() {
    private var isShow = true
    private lateinit var firstFragment: FirstFragment

    companion object {
        fun actionStart(context: Context) {
            startActivity<FirstActivity>(context)
        }
    }


    override fun initView() {
        firstFragment = FirstFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, firstFragment, "first")
            commit()
        }

        GlobalAsyncHandler.postDelayed(1000) {
            setState(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
        }
    }

    override fun initStatusBar() {
        //重写该方法，非纯色沉浸式
    }


    override fun initData() {}

    override fun initListener() {
        super.initListener()
        binding.tv.setOnClickListener {
            if (isShow) {
                binding.tv.text = "显示"
                firstFragment.hideSelf(R.anim.lib_common_no_anim, R.anim.lib_common_push_bottom_out)
            } else {
                binding.tv.text = "隐藏"
                firstFragment.showSelf(R.anim.lib_common_push_bottom_in, R.anim.lib_common_no_anim)
            }
            isShow = !isShow
        }
    }
}