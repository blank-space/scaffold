package com.dawn.base.ui.page.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.dawn.base.BaseApp
import com.dawn.base.R
import com.dawn.base.contentProvider.BaseArchContentProvider
import com.dawn.base.log.L
import com.dawn.base.ui.callback.CallbackConfig
import com.dawn.base.ui.callback.ErrorLayoutCallback
import com.dawn.base.ui.page.delegate.WrapLayoutDelegateImpl
import com.dawn.base.ui.page.delegate.iface.PageType
import com.dawn.base.ui.page.iface.*
import com.dawn.base.utils.inflateBindingWithGeneric
import com.dawn.base.viewmodel.base.BaseViewModel
import com.kingja.loadsir.core.LoadService
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseFragment<T : BaseViewModel, VB : ViewBinding> : Fragment(), IViewState {
    protected var mActivityFragmentManager: FragmentManager? = null
    protected var mAnimationLoaded = false
    var mActivity: WeakReference<Activity>? = null
    lateinit var mViewModel: T
    private val TAG by lazy { javaClass.simpleName }
    private var layoutDelegateImpl: WrapLayoutDelegateImpl? = null
    private var mTitleView: ITitleView? = null
    private var hideOther = true
    val binding: VB by lazy { inflateBindingWithGeneric(layoutInflater) }
    private var mLoadService: LoadService<ViewState>? = null
    private var smartRefreshLayout: SmartRefreshLayout? = null
    private var pageStateChangeListener: PageStateChangeListener? = PageStateChangeListener(this)

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
        mActivityFragmentManager = activity?.supportFragmentManager
        val view = initWrapDelegate()
        val parent = view?.parent
        if (parent != null) {
            parent as ViewGroup
            parent.removeView(view)
        }
        return view
    }

    private fun initWrapDelegate(): View? {
        layoutDelegateImpl = WrapLayoutDelegateImpl(
            mFragment = this,
            childView = getLayout(),
            pageType = getPageType(),
            viewStateChangeListener = pageStateChangeListener,
            loadService = mLoadService,
            callbackConfig = getCallbackConfig()
        )
        return layoutDelegateImpl?.setup()
    }

    protected open fun getCallbackConfig(): CallbackConfig? {
        return null
    }


    protected open fun initListViewDelegate(refreshLayout: SmartRefreshLayout) {}

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBarColor(getStatusBarColor())
        injectARoute()
        initView(view)
        smartRefreshLayout?.let { initListViewDelegate(it) }
        initListener()
        initData()
    }

    /**
     * 是否需要转场动画，默认：false
     */
    open fun isNeedTransition(): Boolean = false

    private fun injectARoute() {
        if (!isNeedInjectARouter()) {
            return
        }
        ARouter.getInstance().inject(this)
    }

    /** 如果有需要ARouter能力的，复写该方法 */
    open fun isNeedInjectARouter(): Boolean = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initStatusBarColor(colorId: Int) {
        if (colorId != Color.TRANSPARENT) mActivity?.get()?.window?.statusBarColor = colorId
    }

    /**
     * 设置状态栏的颜色
     */
    protected open fun getStatusBarColor() = Color.TRANSPARENT

    protected open fun getRefreshType(): Int {
        return RefreshType.NONE
    }

    /**
     * 区分列表页和一般页面
     */
    protected open fun getPageType(): Int {
        return PageType.NORMAL
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

    abstract fun initView(view: View)

    abstract fun initData()

    protected open fun getIntentData() {}

    fun getLayout(): View {
        return binding.root
    }

    open fun initViewModel(): T {
        val persistentClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseArchContentProvider.app)
        ).get(persistentClass)
        return mViewModel
    }


    protected open fun initListener() {
        mViewModel.viewState.observe(viewLifecycleOwner, {
            dispatchPageState(it)
        })
    }

    override fun setState(state: ViewStateWithMsg) {
        mViewModel.viewState.postValue(state)
    }

    private fun dispatchPageState(state: ViewStateWithMsg) {
        state.state?.let {
            L.d("fragment  state:${state.state}")
            layoutDelegateImpl?.showState(it, state.msg)
            modifyTheCallbackDynamically(state.msg)
        }
    }

    override fun modifyTheCallbackDynamically(msg: String?) {

    }

    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pageStateChangeListener = null
    }

}