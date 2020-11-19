package com.bsnl.common.page.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bsnl.base.manager.KeyboardStateManager
import com.bsnl.base.manager.NetworkStateManager
import com.bsnl.common.iface.*
import com.bsnl.common.page.delegate.WrapLayoutDelegateImpl
import com.bsnl.common.page.delegate.iface.OnViewStateListener
import com.bsnl.common.viewmodel.BaseViewModel
import com.jaeger.library.StatusBarUtil
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), ITrack, IViewState {
    private var mTitleView: ITitleView? = null
    lateinit var mViewModel: T
    val TAG by lazy { javaClass.simpleName }
    private var msg: String? = null
    lateinit var mContext: Context
    var mActivity: WeakReference<Activity>? = null
    private  var layoutDelegateImpl: WrapLayoutDelegateImpl?=null


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mContext = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = WeakReference(this)
        mViewModel = initViewModel()
        getIntentData()
        lifecycle.addObserver(KeyboardStateManager)
        lifecycle.addObserver(NetworkStateManager)
        if (getLayout() != null) {
            setContentView(getLayout())
        } else {
            layoutDelegateImpl = WrapLayoutDelegateImpl(
                mActivity = this,
                mContentLayoutResID = getLayoutId(),
                mRefreshType = getRefreshType(),
                mOnViewStateListener = MyViewStateListener()
            )
            layoutDelegateImpl?.setup()
        }
        initView()
        initListener()
        initData()
        setStatusBar()
    }


    /**
     * desc:内容区域是否在标题栏之下
     *
     */
    open fun isContentUnderTitleBar(): Boolean {
        return false
    }


    fun getTitleView(): ITitleView? {
        ensureTitleView(isContentUnderTitleBar())
        return mTitleView
    }

    private fun ensureTitleView(isContentUnderTitleBar: Boolean) {
        if (mTitleView == null) {
            mTitleView =
                layoutDelegateImpl?.getTitleView(isFinalImmersionBarEnable(), isContentUnderTitleBar)
        }
        if (mTitleView != null) {
            if (mTitleView!!.getToolbar() != null) {
                setSupportActionBar(mTitleView!!.getToolbar())
                supportActionBar!!.setDisplayShowTitleEnabled(false)
            }
            mTitleView!!.setNavIconOnClickListener { processClickNavIcon() }

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
    protected fun isInterceptBackPressed() = false

    /**
     * 是否开启沉浸式
     */
    protected fun isFinalImmersionBarEnable(): Boolean {
        return false
    }


    @Deprecated("只满足基础显示，暂不支持页面切换以及下拉刷新等feature")
    protected open fun getLayout(): View? {
        return null
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
        mViewModel.viewState.observe(this, Observer {
            msg = it.msg
            it.state?.let { it1 -> setState(it1) }
        })
    }

    open fun setStatusBar() {
        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setTransparent(this)
    }

    fun getLayoutDelegateImpl() = layoutDelegateImpl

    override fun setState(state: ViewState) {
        layoutDelegateImpl?.showState(state, true, true)

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

    abstract fun initView()

    abstract fun getLayoutId(): Int


    abstract fun initViewModel(): T

    abstract fun initData()

    override fun onDestroy() {
        if (mActivity != null) {
            mActivity?.clear()
            mActivity = null
        }
        super.onDestroy()
    }

}