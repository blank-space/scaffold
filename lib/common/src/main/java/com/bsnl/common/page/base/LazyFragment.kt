package com.bsnl.common.page.base

import com.bsnl.common.viewmodel.BaseViewModel


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 懒加载模式
 * @update : 2020/11/11
 * @note   : setUserVisibleHint()在androidX放弃， Use [androidx.fragment.app.FragmentTransaction.setMaxLifecycle]
 */
abstract class LazyFragment<T : BaseViewModel> : BaseFragment<T>() {

    private var mHasInit = false

    override fun initView() {
        //do nothing
    }

    override fun initData() {
        //do nothing
    }


    abstract fun lazyInitView()


    abstract fun lazyInitData()


    override fun onResume() {
        super.onResume()
        if (!mHasInit) {
            mHasInit = true
            lazyInitView()
            lazyInitData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHasInit = false
    }
}