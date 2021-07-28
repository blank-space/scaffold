package com.bsnl.common.page.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.bsnl.base.manager.KeyboardStateManager
import com.bsnl.common.iface.*
import com.bsnl.common.page.delegate.WrapLayoutDelegateImpl
import com.bsnl.common.page.delegate.iface.OnViewStateListener
import com.bsnl.common.viewmodel.BaseViewModel
import com.jaeger.library.StatusBarUtil
import com.lxj.xpopup.util.KeyboardUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), IViewState {
    private var mTitleView: ITitleView? = null
    lateinit var mViewModel: T
    val TAG by lazy { javaClass.simpleName }
    lateinit var mContext: Context
    var mActivity: WeakReference<Activity>? = null
    private var layoutDelegateImpl: WrapLayoutDelegateImpl? = null
    private var hideOther = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectARoute()
        mContext = this
        mActivity = WeakReference(this)
        mViewModel = initViewModel()
        getIntentData()
        lifecycle.addObserver(KeyboardStateManager)

        initWrapDelegate()
        initView()
        initListener()
        initData()
        initStatusBar()
        initEventBus()
    }



    private fun initWrapDelegate() {
        layoutDelegateImpl = if (getLayout() != null) WrapLayoutDelegateImpl(
            mActivity = this,
            childView = getLayout(),
            mRefreshType = getRefreshType(),
            mOnViewStateListener = MyViewStateListener()

        )
        else WrapLayoutDelegateImpl(
            mActivity = this,
            mContentLayoutResID = getLayoutId(),
            mRefreshType = getRefreshType(),
            mOnViewStateListener = MyViewStateListener()

        )
        initBottomLayout(getBottomLayoutId(), getBottomHeight())
        layoutDelegateImpl?.setup()
    }

    fun initEventBus() {
        if (isNeedEvenBus()) {
            EventBus.getDefault().register(this)
        }
    }

    private fun injectARoute() {
        if (!isNeedInjectARouter()) {
            return
        }
       ARouter.getInstance().inject(this)
    }

    open fun isNeedInjectARouter(): Boolean {
        return true
    }

    /**
     * desc:内容区域是否在标题栏之下
     *
     */
    open fun isContentUnderTitleBar(): Boolean {
        return true
    }

    /**
     * desc:是否开启evenBus
     *
     */
    open fun isNeedEvenBus(): Boolean {
        return false
    }


    open fun getTitleView(): ITitleView? {
        ensureTitleView(isContentUnderTitleBar())
        return mTitleView
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
            if (it.getToolbar() != null) {
                setSupportActionBar(it.getToolbar())
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
            it.setNavIconOnClickListener { processClickNavIcon() }
        }
    }

    protected fun processClickNavIcon() {
        if (isInterceptBackPressed()) {
            return
        }
        onBackPressed()
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
     * 是否开启头部沉浸式
     */
    protected open fun isTranslucentForHeader(): Boolean {
        return false
    }


    protected open fun getIntentData() {

    }

    protected open fun getRefreshType(): Int {
        return RefreshType.NONE
    }


    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }


    protected open fun initListener() {
        mViewModel.viewState.observe(this, {
            setState(it)
        })
    }

    open fun initStatusBar() {
        if (isTranslucentForHeader()) {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            StatusBarUtil.setTranslucentForImageView(this, 0, null)
            return
        }
        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setTransparent(this)
    }

    fun getLayoutDelegateImpl() = layoutDelegateImpl

    override fun setState(state: ViewStateWithMsg) {
        state.state?.let {
            hideOther = it != ViewState.STATE_SHOW_LOADING_DIALOG
            layoutDelegateImpl?.showState(
                it,
                true,
                showBottomViewAnyWay(),
                hideOther,
                state.illustrateStrId,
                state.msg,

                )
        }

    }

    protected open fun showBottomViewAnyWay(): Boolean {
        return false
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

    protected open fun getBottomView(): View? {
        return layoutDelegateImpl?.getBottomLayout()
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

    protected open fun getLayoutId(): Int = 0

    protected open fun getBottomLayoutId(): Int = -1

    protected open fun getBottomHeight(): Int = -1

    private fun initBottomLayout(bottomLayoutId: Int, bottomLayoutHeight: Int) {
        getLayoutDelegateImpl()?.setBottomLayout(bottomLayoutId, bottomLayoutHeight)
    }

    abstract fun getLayout(): View?

    abstract fun initViewModel(): T

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        if (isNeedEvenBus()) {
            EventBus.getDefault().unregister(this)
        }
        if (mActivity != null) {
            mActivity?.clear()
            mActivity = null
        }
        super.onDestroy()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            v?.let {
                if (isShouldHideKeyboard(it, ev)) {
                    KeyboardUtils.hideSoftInput(it)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    protected open fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        if (v !is EditText) {
            return false
        }

        val l = intArrayOf(0, 0)
        v.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val bottom = top + v.getHeight()
        val right = left + v.getWidth()
        return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
    }

}