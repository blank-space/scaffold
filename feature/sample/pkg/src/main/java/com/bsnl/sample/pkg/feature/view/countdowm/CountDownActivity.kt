package com.bsnl.sample.pkg.feature.view.countdowm

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.bsnl.base.utils.GlobalAsyncHandler
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.iface.ViewState
import com.bsnl.common.iface.ViewStateWithMsg
import com.bsnl.common.page.base.BaseListActivity
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.pkg.feature.itemViewBinder.CountDownItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.CountDownViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/2
 * @desc   :
 */
class CountDownActivity : BaseListActivity<CountDownViewModel>() {
    private val countDownItemViewBinder by lazy { CountDownItemViewBinder() }
    private var mAdapter: MultiTypeAdapter? = null

    /**
     * 用来执行倒计时
     */
    private val timerHandler = Handler(Looper.getMainLooper())

    /**
     * 剩余倒计时
     */
    private var delay = 0L

    private val timerRunnable = Runnable {
        delay -= 1000
        updateTimerState()
    }

    private fun updateTimerState() {
        val data = mViewModel.providerData() as MutableList<Long>
        var needCountDownItemSize = 0
        val needUpdateItem = mutableListOf<Int>()
        for (i in 0 until data.size) {
            if (data[i] > 0) {
                ++needCountDownItemSize
                data[i] = data[i]-1000
                needUpdateItem.add(i)
            }
        }
        if(needCountDownItemSize>0){
            needUpdateItem.forEach{
                mAdapter?.notifyItemChanged(it,"update")
            }
            startTimer()
        }else{
            endTimer()
        }

    }

    // 开始倒计时
    private fun startTimer() {
        timerHandler.postDelayed(timerRunnable, 1000)
    }

    // 结束倒计时
    private fun endTimer() {
        timerHandler.removeCallbacks(timerRunnable)
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
        setState(ViewStateWithMsg(null, "", ViewState.STATE_COMPLETED))
    }

    override fun getRefreshType(): Int {
        return RefreshType.NONE
    }

    override fun onGetDataFinish(data: Any?) {
        super.onGetDataFinish(data)
        GlobalAsyncHandler.postDelayed(300) {
            //countDownItemViewBinder.startCountDown(23333)
            startTimer()

        }

    }
}