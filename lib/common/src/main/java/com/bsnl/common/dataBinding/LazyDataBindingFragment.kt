package com.bsnl.common.dataBinding

import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
import com.bsnl.common.viewmodel.BaseViewModel


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 懒加载模式
 */
abstract class LazyDataBindingFragment<T : BaseViewModel> : DataBindingFragment<T>() {

    /**
     * 懒加载过
     */
    private var isLazyLoaded = false

    /**
     * Fragment的View加载完毕的标记
     */
    private var isPrepared = false

    /**
     * 第一步,改变isPrepared标记
     * 当onViewCreated()方法执行时,表明View已经加载完毕,此时改变isPrepared标记为true,并调用lazyLoad()方法
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("lzx","onActivityCreated")
        //isPrepared = true
        //只有Fragment onCreateView好了
        //另外这里调用一次lazyLoad(）
        lazyLoad()
    }


    /**
     * @see lazyInitData()
     */
    override fun initData() {
        //交给lazyInitData()

    }


    /**
     * 第二步
     * 此方法会在onCreateView(）之前执行
     * 当viewPager中fragment改变可见状态时也会调用
     * 当fragment 从可见到不见，或者从不可见切换到可见，都会调用此方法
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        lazyLoad()
    }

    /**
     * 调用懒加载
     * 第三步:在lazyLoad()方法中进行双重标记判断,通过后即可进行数据加载
     */
    private fun lazyLoad() {
        if (userVisibleHint && isPrepared && !isLazyLoaded) {
            lazyInitData()
            isLazyLoaded = true
        }
    }

    /**
     * 第四步:定义抽象方法onLazyLoad(),具体加载数据的工作,交给子类去完成
     */
    @UiThread
    abstract fun lazyInitData()
}