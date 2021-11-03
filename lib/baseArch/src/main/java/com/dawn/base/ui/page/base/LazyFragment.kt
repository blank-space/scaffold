package com.dawn.base.ui.page.base

import android.view.View
import androidx.viewbinding.ViewBinding
import com.dawn.base.viewmodel.base.BaseViewModel


/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 懒加载模式
 * @update : 2020/11/11
 * @note   : setUserVisibleHint()在androidX放弃， Use [androidx.fragment.app.FragmentTransaction.setMaxLifecycle]
 */
abstract class LazyFragment<T : BaseViewModel,VB: ViewBinding> : BaseFragment<T,VB>() {

    private var mHasInit = false

    override fun initView(v: View) {
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
            lazyInitListener()
            lazyInitData()
        }
    }

    protected open fun lazyInitListener(){}

    override fun onDestroyView() {
        super.onDestroyView()
        mHasInit = false
    }
}