package com.dawn.sample.pkg.feature.view.hexStatusManager

import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.onClick
import com.dawn.base.viewmodel.EmptyViewModel
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.databinding.PkgActivityStatusManagerBinding

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/4
 * @desc   : 十六进制数管理状态
 */
@Route(path = SamplePath.A_HEX_STATUS_MANAGER_ACTIVITY)
class HexStatusManagerActivity : BaseActivity<EmptyViewModel, PkgActivityStatusManagerBinding>() {
    private val modelManager = ModelManager()
    override fun initView() {
        getTitleView()?.setTitleText(TAG)
    }



    override fun initListener() {
        super.initListener()
        binding.tvAdd.onClick = {
            modelManager.add()
            binding.tvResult.text = "after add STATUS_1:${modelManager.statuses}"

        }

        binding.tvRemove.onClick = {
            modelManager.remove()
            binding.tvResult.text = "after remove STATUS_1:${modelManager.statuses}"
        }

        binding.tvCheck.onClick = {

            binding.tvResult.text = "isContain STATUS_1 :${modelManager.isContain()}"
        }
        binding.tvResetToModelA.onClick = {
            modelManager.change2ModelA()
            binding.tvResult.text = "after change to modelA :${modelManager.statuses}"

        }

    }
}