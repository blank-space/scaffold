package com.dawn.sample.pkg.feature.view.download

import android.content.Context
import android.content.Intent
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.getApplicationScopeViewModel
import com.dawn.base.utils.onClick
import com.dawn.base.utils.startActivity
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityDownloadBinding
import com.dawn.sample.pkg.feature.domain.message.ShareViewModel
import com.dawn.sample.pkg.feature.viewmodel.DownLoadViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/26
 * @desc   :
 */
class DownLoadActivity : BaseActivity<DownLoadViewModel, FeatureSamplePkgActivityDownloadBinding>() {
    private val event: ShareViewModel by lazy {
        getApplicationScopeViewModel()
    }

    companion object {
        fun startAction(context: Context) {
            startActivity<DownLoadActivity>(context)
        }
    }

    override fun initView() {
        lifecycle.addObserver(mViewModel.downloadRequest.canBeStoppedUseCase)
    }

    override fun initData() {
        super.initData()
        mViewModel.downloadRequest.downloadLiveData.observe(this) {
            if (it.isSuccessFul) {
                binding.tvDownLoad.text = "download progress :${it.data.process}%"
                //test发送事件
                event.countLiveData.value = it.data.process
            }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.tvDownLoad.onClick = {
            mViewModel.downloadRequest.requestCanBeStoppedDownloadFile()
        }
        binding.tvClose.onClick = {
            val intent = Intent()
            intent.putExtra("data", "data from DownLoadActivity")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}