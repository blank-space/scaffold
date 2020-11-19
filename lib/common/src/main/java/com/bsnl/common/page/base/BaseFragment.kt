package com.bsnl.common.page.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.bsnl.base.manager.KeyboardStateManager
import com.bsnl.common.iface.*
import com.bsnl.common.page.delegate.WrapLayoutDelegateImpl
import com.bsnl.common.page.delegate.iface.OnViewStateListener
import com.bsnl.common.utils.doOnMainThreadIdle
import com.bsnl.common.viewmodel.BaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseFragment<T : BaseViewModel> : Fragment(), ITrack, IViewState {
    protected var mActivityFragmentManager: FragmentManager? = null
    protected var mAnimationLoaded = false
    var mActivity: WeakReference<Activity>? = null
    lateinit var mViewModel: T
    private val TAG by lazy { javaClass.simpleName }
    private var msg: String? = null
    private  var layoutDelegateImpl: WrapLayoutDelegateImpl?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = WeakReference(context as FragmentActivity)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = initViewModel()
        getIntentData()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(KeyboardStateManager)
        mActivityFragmentManager = activity?.supportFragmentManager
        layoutDelegateImpl = WrapLayoutDelegateImpl(
            mFragment = this,
            mContentLayoutResID = getLayoutId(),
            mRefreshType = getRefreshType(),
            mOnViewStateListener = MyViewStateListener()
        )
        val view = getLayout() ?: layoutDelegateImpl?.setup()
        val parent = view?.getParent()
        if (parent != null) {
            parent as ViewGroup
            parent.removeView(view)
        }
        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
        val targetLayout = view
        //先隐藏
        targetLayout.isInvisible = true
        //实现流畅的转场动画
        doOnMainThreadIdle({
            TransitionManager.beginDelayedTransition(
                targetLayout.parent as ViewGroup,
                Slide(Gravity.BOTTOM).apply {
                    addTarget(targetLayout)
                })
            //动画执行结束后显示view
            targetLayout.isInvisible = false
        }, 100)
    }

    protected open fun getRefreshType(): Int {
        return RefreshType.NONE
    }

    private inner class MyViewStateListener : OnViewStateListener {
        override fun onReload(v: View?) {
            onPageReload(v)
        }


        override fun onNoDataBtnClick(v: View?) {
            processNoDataBtnClick(v)
        }


        override fun onRefresh(refreshLayout: IRefreshLayout?) {
            processRefresh(refreshLayout)
        }

        override fun onLoadMore(refreshLayout: IRefreshLayout?) {
            processLoadMore(refreshLayout)
        }
    }

    protected open fun onPageReload(v: View?) {

    }

    protected open fun processNoDataBtnClick(v: View?) {

    }

    protected open fun processRefresh(refreshLayout: IRefreshLayout?) {

    }

    protected open fun processLoadMore(refreshLayout: IRefreshLayout?) {

    }

    fun getLayoutDelegateImpl() = layoutDelegateImpl


    fun hideSelf(@AnimatorRes @AnimRes enter: Int, @AnimatorRes @AnimRes exit: Int) {
        try {
            val fragmentTransaction: FragmentTransaction =
                this.mActivityFragmentManager!!.beginTransaction()
            fragmentTransaction.setCustomAnimations(enter, exit)
            fragmentTransaction.hide(this).commitAllowingStateLoss()
        } catch (var4: Exception) {
            var4.printStackTrace()
        }
    }


    fun showSelf(@AnimatorRes @AnimRes enter: Int, @AnimatorRes @AnimRes exit: Int) {
        try {
            val fragmentTransaction: FragmentTransaction =
                this.mActivityFragmentManager!!.beginTransaction()
            fragmentTransaction.setCustomAnimations(enter, exit)
            fragmentTransaction.show(this).commitAllowingStateLoss()
        } catch (var4: Exception) {
            var4.printStackTrace()
        }
    }


    abstract fun initView()


    abstract fun initData()

    abstract fun getLayoutId(): Int

    abstract fun initViewModel(): T

    protected open fun getIntentData() {

    }

    @Deprecated("只满足基础显示，暂不支持页面切换以及下拉刷新等feature")
    protected open fun getLayout(): View? {
        return null
    }


    protected open fun initListener() {
        mViewModel.viewState.observe(requireActivity(), Observer {
            msg = it.msg
            it.state?.let { it1 -> setState(it1) }
        })

    }

    override fun setState(state: ViewState) {
        layoutDelegateImpl?.showState(state, true, true)
    }



    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }

}