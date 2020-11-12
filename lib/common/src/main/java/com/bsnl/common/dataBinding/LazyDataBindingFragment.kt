package com.bsnl.common.dataBinding

import android.os.Bundle
import com.bsnl.common.viewmodel.BaseViewModel


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 懒加载模式
 * @update : 2020/11/11
 */
@Deprecated("setUserVisibleHint()在androidX放弃， Use {@link FragmentTransaction#setMaxLifecycle(Fragment, Lifecycle.State)}")
abstract class LazyDataBindingFragment<T : BaseViewModel> : DataBindingFragment<T>() {
    //fragment界面初始化结束后设为true
    protected var isViewInitiated = false
    //当前界面是否可见
    protected var isVisibleToUser = false
    //是否获取过数据
    protected var isDataInitiated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        prepareFetchData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        prepareFetchData()
    }

    abstract fun fetchData()

    open fun prepareFetchData(): Boolean {
        return prepareFetchData(false)
    }


    /**
     * 强制刷新数据
     */
    open fun forceRefreshData() {
        prepareFetchData(true)
    }

    private  fun prepareFetchData(forceUpdate: Boolean): Boolean {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData()
            isDataInitiated = true
            return true
        }
        return false
    }
}