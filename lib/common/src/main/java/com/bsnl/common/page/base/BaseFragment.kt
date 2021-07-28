package com.bsnl.common.page.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.bsnl.base.manager.KeyboardStateManager
import com.bsnl.common.R
import com.bsnl.common.iface.*
import com.bsnl.common.page.delegate.WrapLayoutDelegateImpl
import com.bsnl.common.page.delegate.iface.OnViewStateListener
import com.bsnl.common.utils.doOnMainThreadIdle
import com.bsnl.common.viewmodel.BaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.EventBus
import java.lang.NullPointerException
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseFragment<T : BaseViewModel> : Fragment(), IViewState {
    protected var mActivityFragmentManager: FragmentManager? = null
    protected var mAnimationLoaded = false
    var mActivity: WeakReference<Activity>? = null
    lateinit var mViewModel: T
    private val TAG by lazy { javaClass.simpleName }
    private var layoutDelegateImpl: WrapLayoutDelegateImpl? = null
    private var mTitleView: ITitleView? = null
    private var hideOther = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = WeakReference(context as FragmentActivity)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = initViewModel()
        getIntentData()
    }


    open fun getTitleView(): ITitleView? {
        ensureTitleView(isContentUnderTitleBar())
        return mTitleView
    }

    /**
     * desc:内容区域是否在标题栏之下
     *
     */
    open fun isContentUnderTitleBar(): Boolean {
        return true
    }

    /**
     * 初始化TitleView以及点击事件
     */
    private fun ensureTitleView(isContentUnderTitleBar: Boolean) {
        if (mTitleView == null) {
            mTitleView = layoutDelegateImpl?.getTitleView(
                isFinalImmersionBarEnable(),
                isContentUnderTitleBar
            )
        }
        mTitleView?.let {
            it.setNavIconOnClickListener { processClickNavIcon() }
        }
    }

    protected open fun processClickNavIcon() {
        if (isInterceptBackPressed()) {
            return
        }
        hideSelf(R.anim.lib_common_no_anim, R.anim.lib_common_no_anim)
    }

    /**
     * 是否拦截返回键
     */
    protected open fun isInterceptBackPressed() = false

    /**
     * 是否开启沉浸式
     */
    protected open fun isFinalImmersionBarEnable(): Boolean {
        return true
    }

    /**
     * 是否显示顶部image导航栏
     */
    protected open fun isShowImageHeader(): Boolean {
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(KeyboardStateManager)
        mActivityFragmentManager = activity?.supportFragmentManager
        val view = initWrapDelegate()
        val parent = view?.getParent()
        if (parent != null) {
            parent as ViewGroup
            parent.removeView(view)
        }
        return view
    }

    private fun initWrapDelegate(): View? {
        layoutDelegateImpl = if (getLayout() != null) WrapLayoutDelegateImpl(
            mFragment = this,
            childView = getLayout(),
            mRefreshType = getRefreshType(),
            mOnViewStateListener = MyViewStateListener(),
            showImgHeader = isShowImageHeader()
        )
        else WrapLayoutDelegateImpl(
            mFragment = this,
            mContentLayoutResID = getLayoutId(),
            mRefreshType = getRefreshType(),
            mOnViewStateListener = MyViewStateListener(),
            showImgHeader = isShowImageHeader()
        )
        initBottomLayout(getBottomLayoutId(), getBottomHeight())
        return layoutDelegateImpl?.setup()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBarColor(getStatusBarColor())
        injectARoute()
        initView(view)
        initListener()
        initData()
        initEventBus()

        val targetLayout = view
        if (!isNeedTransition()) return
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

    /**
     * 是否需要转场动画，默认：false
     */
    open fun isNeedTransition(): Boolean = false


    private fun injectARoute() {
        if (!isNeedInjectARouter()) {
            return
        }
        //ARouter.getInstance().inject(this)
    }

    open fun isNeedInjectARouter(): Boolean {
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initStatusBarColor(colorId: Int) {
        if (colorId != Color.TRANSPARENT) mActivity?.get()?.window?.setStatusBarColor(colorId)
    }

    /**
     * 设置状态栏的颜色
     */
    protected open fun getStatusBarColor() = Color.TRANSPARENT


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

        override fun onLoadCustomLayout(v: View?) {
            processCustomLayout(v)
        }
    }

    protected open fun processCustomLayout(v: View?) {}

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
        this.mActivityFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(enter, exit)
            hide(this@BaseFragment)
            commitAllowingStateLoss()
        }
    }


    fun showSelf(@AnimatorRes @AnimRes enter: Int, @AnimatorRes @AnimRes exit: Int) {
        this.mActivityFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(enter, exit)
            show(this@BaseFragment)
            commitAllowingStateLoss()
        }
    }

    fun initEventBus() {
        if (isNeedEvenBus()) {
            EventBus.getDefault().register(this)
        }
    }

    abstract fun initView(view: View)


    abstract fun initData()

    protected open fun getLayoutId(): Int = 0

    protected open fun getBottomLayoutId(): Int = -1

    protected open fun getBottomHeight(): Int = -1

    /**
     * desc:是否开启evenBus
     *
     */
    open fun isNeedEvenBus(): Boolean {
        return false
    }


    private fun initBottomLayout(bottomLayoutId: Int, bottomLayoutHeight: Int) {
        getLayoutDelegateImpl()?.setBottomLayout(bottomLayoutId, bottomLayoutHeight)
    }

    abstract fun initViewModel(): T

    protected open fun getIntentData() {

    }

    abstract fun getLayout(): View?


    protected open fun initListener() {
        mViewModel.viewState.observe(viewLifecycleOwner, {
            it.let { it1 -> setState(it1) }
        })

    }

    override fun setState(state: ViewStateWithMsg) {
        state.state?.let {

            hideOther = it != ViewState.STATE_SHOW_LOADING_DIALOG
            layoutDelegateImpl?.showState(
                it,
                true,
                showBottomViewAnyWay(),
                hideOther,
                state.illustrateStrId,
                state.msg
            )
        }
    }



    protected open fun showBottomViewAnyWay(): Boolean {
        return false
    }

    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isNeedEvenBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

}