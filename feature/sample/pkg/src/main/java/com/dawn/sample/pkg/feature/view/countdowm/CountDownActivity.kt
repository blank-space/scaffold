package com.dawn.sample.pkg.feature.view.countdowm

import android.content.Context
import android.os.CountDownTimer
import android.view.View
import com.dawn.base.log.L
import com.dawn.base.ui.callback.CallbackConfig
import com.dawn.base.ui.callback.EmptyLayoutCallback
import com.dawn.base.ui.callback.ErrorLayoutCallback
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.base.SimpleListActivity
import com.dawn.base.utils.ItemClickSupport
import com.dawn.base.utils.showToast
import com.dawn.base.utils.startActivity
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.feature.callback.PlaceholderCallback
import com.dawn.sample.pkg.feature.itemViewBinder.CountDownItemViewBinder
import com.dawn.sample.pkg.feature.viewmodel.CountDownViewModel
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




    override fun getCallbackConfig(): CallbackConfig? {
        return CallbackConfig(callbackLoading = PlaceholderCallback())
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

    override fun onPageReload(v: View?) {
        super.onPageReload(v)
        L.e("onPageReload,onPageReload")
    }

    override fun initListener() {
        super.initListener()
        ItemClickSupport.addTo(getRecyclerView())
            .setOnItemClickListener(object : ItemClickSupport.OnItemClick<MultiTypeAdapter>() {
                override fun onItemClick(adapter: MultiTypeAdapter?, view: View?, position: Int) {
                    "$position onItemClick".showToast()
                }

                override fun onItemChildClick(
                    adapter: MultiTypeAdapter?,
                    view: View?,
                    position: Int) {
                    when (view?.id) {
                        R.id.img -> {
                            "$position img".showToast()
                        }

                        R.id.tv_title -> {
                            "$position tv_title".showToast()
                        }
                    }
                }

                override fun onItemLongClick(
                    adapter: MultiTypeAdapter?,
                    view: View?,
                    position: Int
                ) {
                    "$position onItemLongClick".showToast()
                }

                override fun onItemChildLongClick(
                    adapter: MultiTypeAdapter?,
                    view: View?,
                    position: Int
                ) {
                    "$position onItemChildLongClick".showToast()
                }
            })
    }
}