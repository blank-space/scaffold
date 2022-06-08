package com.dawn.sample.pkg.feature.view

import android.content.Context
import android.util.Log
import androidx.activity.viewModels
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.startActivity
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityMainBinding
import com.dawn.sample.pkg.feature.viewmodel.TestViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/26
 * @desc   :
 * @note   : 使用SavedStateHandle可以在生命周期重建的时候保存数据，试验activity和fragment使用不同的viewModel,其中fragment
 * 使用Fragment Scope,无法达到预期结果。 只有让activity和fragment共享同个viewModel才行。
 */

class FirstActivity : BaseActivity<TestViewModel, FeatureSamplePkgActivityMainBinding>() {
    private var isShow = true
    private lateinit var firstFragment: FirstFragment
    private val vm : TestViewModel by viewModels()
    companion object {
        fun actionStart(context: Context) {
            startActivity<FirstActivity>(context)
        }
    }

    override fun initView() {
        firstFragment = FirstFragment.newInstance("FirstFragment",-1) as FirstFragment
        Log.d("@@","firstFragment:${firstFragment.hashCode()}")
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, firstFragment, "first")
            commit()
        }
    }



    override fun initViewModel(): TestViewModel {
        return vm
    }


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