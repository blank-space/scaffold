package com.bsnl.sample.pkg.feature.view.countdowm

import android.content.Context
import android.os.CountDownTimer
import com.bsnl.base.log.L
import com.bsnl.base.utils.GlobalAsyncHandler
import com.bsnl.base.utils.showToast
import com.bsnl.common.callback.EmptyLayoutCallback
import com.bsnl.common.callback.ErrorLayoutCallback
import com.bsnl.common.callback.LoadingLayoutCallback
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.page.base.BaseListActivity
import com.bsnl.common.page.base.SimpleListActivity
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.feature.callback.PlaceholderCallback
import com.bsnl.sample.pkg.feature.itemViewBinder.CountDownItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.CountDownViewModel
import com.drakeet.multitype.MultiTypeAdapter
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.Convertor
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/2
 * @desc   :
 */
class CountDownActivity : SimpleListActivity<CountDownViewModel>() {
    private val countDownItemViewBinder by lazy { CountDownItemViewBinder() }
    private var mAdapter: MultiTypeAdapter? = null
    private var countDownTimer: CountDownTimer? = null

    private fun updateTimerState() {
        val data = mViewModel.providerData() as MutableList<Long>
        var needCountDownItemSize = 0
        val needUpdateItem = mutableListOf<Int>()
        for (i in 0 until data.size) {
            if (data[i] > 0) {
                ++needCountDownItemSize
                data[i] = data[i] - 1000
                needUpdateItem.add(i)
            }
        }
        if (needCountDownItemSize > 0) {
            needUpdateItem.forEach {
                mAdapter?.notifyItemChanged(it, "update")
            }
        }

    }

    private fun startTimer() {

        countDownTimer = object : CountDownTimer(getMaxDuration(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                L.d("onTick：$millisUntilFinished")
                updateTimerState()
            }

            override fun onFinish() {
                endTimer()
            }
        }
        countDownTimer?.start()
    }

    private fun getMaxDuration(): Long {
        val data = mViewModel.providerData() as MutableList<Long>
        var duration = 0L
        for (i in 0 until data.size) {
            if (data[i] > 0 && data[i] > duration) {
                duration = data[i]
            }
        }
        L.d("maxDuration:$duration")
        return duration
    }

    private fun endTimer() {
        countDownTimer?.cancel()
    }


    companion object {
        fun actionStart(context: Context) {
            startActivity<CountDownActivity>(context)
        }
    }

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.register(countDownItemViewBinder)
        mAdapter = adapter
    }


    override fun initView() {
        super.initView()
        getTitleView()?.setTitleText("倒计时列表")

    }

    override fun setupLoadSir() {
        val loadSir = LoadSir
            .Builder()
            .addCallback(PlaceholderCallback())
            .setDefaultCallback(PlaceholderCallback::class.java)
            .build()
        //注意：上面的只定义了PlaceholderCallback，在转换器回调里是无法使用其他Callback(SuccessCallback是内置的),否则会抛出异常
        getLayoutDelegateImpl()?.let {
           it.loadService = loadSir.register(it.childView, Callback.OnReloadListener {

           }, Convertor<ViewState> { v ->
               val resultCode: Class<out Callback?> = when (v) {
                   ViewState.STATE_LOADING -> PlaceholderCallback::class.java
                   else -> SuccessCallback::class.java
               }
               resultCode
           }) as LoadService<ViewState>?
        }
    }

    override fun isUseDefaultLoadService(): Boolean {
        return false
    }



    override fun onGetDataFinish(data: Any?) {
        super.onGetDataFinish(data)
        L.d("onGetDataFinish")
        GlobalAsyncHandler.postDelayed(1000) {
            endTimer()
            startTimer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        countDownTimer = null
    }
}