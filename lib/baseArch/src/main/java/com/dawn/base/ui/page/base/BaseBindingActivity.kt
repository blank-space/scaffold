package com.dawn.base.ui.page.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.manager.KeyboardStateManager
import com.dawn.base.ui.page.FragmentStateFixer
import com.dawn.base.utils.AdaptScreenUtils
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.ui.page.iface.*
import com.dawn.base.ui.page.delegate.WrapLayoutDelegateImpl
import com.dawn.base.utils.KeyboardUtils
import com.dawn.base.utils.inflateBindingWithGeneric
import com.dawn.base.viewmodel.base.BaseViewModel
import com.jaeger.library.StatusBarUtil
import com.kingja.loadsir.core.LoadService
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseBindingActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(),
    IViewState {
    private var mTitleView: ITitleView? = null
    lateinit var mViewModel: VM
    val TAG by lazy { javaClass.simpleName }
    lateinit var mContext: Context
    var mActivity: WeakReference<Activity>? = null
    private var layoutDelegateImpl: WrapLayoutDelegateImpl? = null
    private var hideOther = true
    val binding: VB by lazy { inflateBindingWithGeneric(layoutInflater) }
    var mLoadService: LoadService<ViewState>? = null
    private var pageStateChangeListener: PageStateChangeListener? = PageStateChangeListener(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        FragmentStateFixer.fixState(this, savedInstanceState)
        super.onCreate(savedInstanceState)
        injectARoute()
        mContext = this
        mActivity = WeakReference(this)
        mViewModel = initViewModel()
        getIntentData()
        lifecycle.addObserver(KeyboardStateManager)
        initWrapDelegate()
        if (!isUseDefaultLoadService()) {
            setupLoadSir()
        }
        initView()
        initListener()
        initData()
        initStatusBar()
    }

    /** 如果[isUseDefaultLoadService]返回false，必须重写该方法去实例化mLoadService，且要使用[LoadSir#register( target,  onReloadListener,convertor)]这个方法去注册*/
    open fun setupLoadSir() {}

    open fun isUseDefaultLoadService() = true

    private fun initWrapDelegate() {
        layoutDelegateImpl = WrapLayoutDelegateImpl(
            mActivity = this,
            childView = getLayout(),
            mRefreshType = getRefreshType(),
            viewStateChangeListener = pageStateChangeListener,
            loadService = mLoadService,
            useLoadService = isUseDefaultLoadService()
        )
        layoutDelegateImpl?.setup()
    }

    private fun injectARoute() {
        if (!isNeedInjectARouter()) {
            return
        }
        ARouter.getInstance().inject(this)
    }

    /** 如果有需要ARouter能力的，复写该方法 */
    open fun isNeedInjectARouter() = false

    /**
     * desc:内容区域是否在标题栏之下
     */
    open fun isContentUnderTitleBar() = true

    /**
     * desc:是否开启evenBus
     */
    open fun isNeedEvenBus() = false


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
    protected open fun isFinalImmersionBarEnable() = true

    /**
     * 是否开启头部沉浸式
     */
    protected open fun isTranslucentForHeader() = false

    protected open fun getIntentData() {}

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
            layoutDelegateImpl?.showState(it, state.msg)
        }
    }

    open fun getLayout(): View? {
        return binding.root
    }

    /*利用反射获取类实例*/
    private fun initViewModel(): VM {
        val persistentClass = (javaClass.genericSuperclass as ParameterizedType).
        actualTypeArguments[0] as Class<VM>
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(persistentClass)
        return mViewModel
    }

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        pageStateChangeListener = null
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

    /** 屏幕适配 */
    override fun getResources(): Resources {
        return if (DisplayUtils.isPortrait()) {
            AdaptScreenUtils.adaptWidth(super.getResources(), 360)
        } else {
            AdaptScreenUtils.adaptHeight(super.getResources(), 640)
        }
    }
}