package com.dawn.base.ui.page.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.manager.KeyboardStateManager
import com.dawn.base.ui.callback.CallbackConfig
import com.dawn.base.ui.page.FragmentStateFixer
import com.dawn.base.ui.page.delegate.WrapLayoutDelegateImpl
import com.dawn.base.ui.page.delegate.iface.PageType
import com.dawn.base.ui.page.iface.*
import com.dawn.base.utils.AdaptScreenUtils
import com.dawn.base.utils.DisplayUtils
import com.dawn.base.utils.KeyboardUtils
import com.dawn.base.utils.inflateBindingWithGeneric
import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.statusbar.*
import com.kingja.loadsir.core.LoadService
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(), IViewState {
    private var mTitleView: ITitleView? = null
    lateinit var mViewModel: VM
    val TAG by lazy { javaClass.simpleName }
    lateinit var mContext: Context
    private var layoutDelegateImpl: WrapLayoutDelegateImpl? = null
    var mActivity: WeakReference<Activity>? = null
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
        initStatusBar()
        getIntentData()
        lifecycle.addObserver(KeyboardStateManager)
        initWrapDelegate()
        initView()
        initListener()
        initData()

    }


    private fun initWrapDelegate() {
        layoutDelegateImpl = WrapLayoutDelegateImpl(
            mActivity = this,
            childView = getLayout(),
            pageType = getPageType(),
            viewStateChangeListener = pageStateChangeListener,
            loadService = mLoadService,
            callbackConfig = getCallbackConfig()
        )
        layoutDelegateImpl?.setup()
    }

    protected open fun getCallbackConfig(): CallbackConfig? {
        return null
    }


    private fun injectARoute() {
        if (!isNeedInjectARouter()) {
            return
        }
        ARouter.getInstance().inject(this)
    }

    /** 如果有需要ARouter能力的，复写该方法 */
    open fun isNeedInjectARouter() = true

    /**
     * 内容区域是否在标题栏之下
     */
    open fun isContentUnderTitleBar() = true


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

    open fun processClickNavIcon() {
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

    /**
     * 区分列表页和一般页面
     */
    protected open fun getPageType(): Int {
        return PageType.NORMAL
    }

    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }

    protected open fun initListener() {
        mViewModel.viewState.observe(this) {
            dispatchPageState(it)
        }
    }

    private fun initStatusBar() {
        immersiveStatusBar()
        immersiveNavigationBar()
        customStatusBar()
    }

    /**
     * 自定义状态栏的具体样式
     */
    open fun customStatusBar() {
        fitStatusBar(true)
        setStatusBarColor(Color.parseColor("#00000000"))
        setLightStatusBar(true)
    }

    fun getLayoutDelegateImpl() = layoutDelegateImpl

    override fun setState(state: ViewStateWithMsg) {
        mViewModel.viewState.postValue(state)
    }

    private fun dispatchPageState(state: ViewStateWithMsg) {
        state.state?.let {
            layoutDelegateImpl?.showState(it, state.msg)
            modifyTheCallbackDynamically(state.msg)
        }
    }

    override fun modifyTheCallbackDynamically(msg: String?) {

    }

    open fun getLayout(): View? {
        return binding.root
    }

    /*利用反射获取类实例*/
    open fun initViewModel(): VM {
        val persistentClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(persistentClass)
        return mViewModel
    }

    abstract fun initView()

    open fun initData() {

    }

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