package com.dawn.base.ui.page.delegate

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dawn.base.R
import com.dawn.base.log.L
import com.dawn.base.ui.callback.*
import com.dawn.base.ui.page.iface.*
import com.dawn.base.ui.page.delegate.iface.OnViewStateListener
import com.dawn.base.ui.page.delegate.iface.IWrapLayoutDelegate
import com.dawn.base.utils.dp
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.Convertor
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.dawn.base.ui.page.delegate.iface.PageType
import com.dawn.base.utils.GlobalAsyncHandler
import com.dawn.base.utils.NetworkUtils
import com.dawn.base.utils.onClick
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.callback.ProgressCallback

import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.Transport


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
class WrapLayoutDelegateImpl(
    val mActivity: AppCompatActivity? = null,
    val mFragment: Fragment? = null,
    val childView: View? = null,
    @PageType.Val val pageType: Int = PageType.NORMAL,
    var viewStateChangeListener: OnViewStateListener? = null,
    var loadService: LoadService<ViewState>? = null,
    val callbackConfig: CallbackConfig? = null

) : IWrapLayoutDelegate {
    //当前页面状态
    private var mCurrentState = ViewState.STATE_LOADING
    private var mContext: Context = mActivity ?: mFragment?.requireContext()!!
    private var mViewConfig: ViewConfig? = null

    //页面base View
    private var mView: View? = null
    private var mLayoutInflater: LayoutInflater? = null

    //内容布局
    private var mContentView: View? = null

    //标题布局
    private var mTitleView: View? = null
    private var mContentWrapView: View? = null
    private var smartRefreshLayout: SmartRefreshLayout? = null
    private var errorMessage: String? = null

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
        mViewConfig = ViewConfig()
    }

    @SuppressLint("CutPasteId")
    fun setup(): View? {
        if (mView == null) {
            var mainView: View? = null
            if (mActivity != null) {
                mActivity.setContentView(getContentLayoutResId())
                mainView = mActivity.findViewById(R.id.root_main)
            } else if (mFragment != null) {
                mainView = mLayoutInflater?.inflate(getContentLayoutResId(), null)
            }
            mView = mainView
            setupContentView()
            if (isNeedRefreshLayout()) {
                smartRefreshLayout =
                    mainView?.findViewById<View>(R.id.content_wrap) as SmartRefreshLayout
            }
            mContentWrapView = mainView?.findViewById(R.id.fl_content)
            if (childView != null && mContentWrapView != null) {
                addChildViewToBaseRootView(mContentWrapView as ViewGroup)
            }

            initLoadServiceConfig()
            modifyTheCallbackDynamically()
        }

        return mView
    }

    private fun initLoadServiceConfig(){
        childView?.let {
            loadService = getLoadSir().register(it, null, Convertor<ViewState> { v ->
                val resultCode: Class<out Callback?> = when (v) {
                    ViewState.STATE_LOADING -> getLoadingClass()
                    ViewState.STATE_COMMIT -> getTransparentLoadingClass()
                    ViewState.STATE_ERROR -> getErrorClass()
                    ViewState.STATE_EMPTY -> getEmptyClass()
                    else -> SuccessCallback::class.java
                }
                resultCode
            }) as LoadService<ViewState>?
        }
    }

    private fun modifyTheCallbackDynamically(){
        loadService?.setCallBack(getErrorClass()) { _, view ->
            view?.let{
                if (NetworkUtils.isConnected()) {
                    it.findViewById<ImageView>(R.id.imgv_reload)
                        ?.setImageResource(R.drawable.base_icon_error)
                } else {
                    it.findViewById<ImageView>(R.id.imgv_reload)
                        ?.setImageResource(R.drawable.base_icon_no_wifi)
                }
                it.findViewById<TextView>(R.id.tv_retry)?.onClick ={
                    viewStateChangeListener?.onReload(it)
                }
            }

        }
    }

    private fun getLoadSir(): LoadSir {
        if (callbackConfig == null) return LoadSir.getDefault()
        return LoadSir
            .Builder()
            .addCallback(callbackConfig.callbackEmpty)
            .addCallback(callbackConfig.callbackLoading)
            .addCallback(callbackConfig.callbackError)
            .addCallback(callbackConfig.callbackTransparentLoading)
            .build()
    }

    private fun getEmptyClass(): Class<out Callback> {
        return callbackConfig?.callbackEmpty?.javaClass ?: EmptyLayoutCallback::class.java
    }

    private fun getLoadingClass(): Class<out Callback> {
        return callbackConfig?.callbackLoading?.javaClass ?: LoadingLayoutCallback::class.java
    }

    private fun getTransparentLoadingClass(): Class<out Callback> {
        return callbackConfig?.callbackTransparentLoading?.javaClass
            ?: TransparentLoadingLayoutCallback::class.java
    }

    private fun getErrorClass(): Class<out Callback> {
        return callbackConfig?.callbackError?.javaClass ?: ErrorLayoutCallback::class.java
    }


    private fun addChildViewToBaseRootView(parentViewGroup: ViewGroup) {
        (childView?.parent as ViewGroup?)?.removeView(childView)
        parentViewGroup.addView(childView)
    }

    private fun setupContentView() {
        mContentView = childView
        val background = mContentView?.background
        if (background != null) {
            mView?.background = background
        }
    }


    fun setTitleLayout(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): View? {
        if (mTitleView != null) {
            return mTitleView
        }
        mViewConfig?.titleLayoutId?.let { setViewStubLayoutRes(R.id.stub_title, it) }
        mTitleView = inflateViewStub(R.id.stub_title)
        layoutTitleView(isImmersionBarEnable, isContentUnderTitleBar, true)
        return mTitleView
    }


    private fun layoutTitleView(
        isImmersionBarEnable: Boolean,
        isContentUnderTitleBar: Boolean,
        fromInit: Boolean
    ) {
        if (mContentWrapView == null) {
            return
        }
        val smartLayoutLp = smartRefreshLayout?.layoutParams as? MarginLayoutParams
        val mlp = mContentWrapView?.layoutParams as MarginLayoutParams
        val titleLp = mTitleView?.layoutParams as MarginLayoutParams
        if (isImmersionBarEnable) {
            if (!isContentUnderTitleBar) {
                mlp.topMargin = 0
                smartLayoutLp?.topMargin = 0
//                titleLp.topMargin = getStatusBarHeight()
                mTitleView?.setPadding(0, getStatusBarHeight(), 0, 0)

            } else {
                if (smartRefreshLayout == null) {
                    mlp.topMargin = 42.5f.dp.toInt()
                } else {
                    smartLayoutLp?.topMargin = 42f.dp.toInt()
                }
                mTitleView?.bringToFront()
            }
        } else {
            if (!isContentUnderTitleBar) {
                mlp.topMargin = 42f.dp.toInt()
            } else {
                mlp.topMargin = 0
                mTitleView?.bringToFront()
            }
        }
        if (!fromInit) {
            mContentWrapView?.parent?.requestLayout()
        }
    }


    private fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }


    private fun setViewStubLayoutRes(@IdRes stubId: Int, @LayoutRes layoutResId: Int) {
        val viewStub = mView?.findViewById<View>(stubId) as? ViewStub
        viewStub?.layoutResource = layoutResId
    }

    private fun inflateViewStub(@IdRes stubId: Int): View? {
        val viewStub = mView?.findViewById<View>(stubId) as? ViewStub
        if (viewStub != null) {
            return if (viewStub.layoutResource > 0) {
                viewStub.inflate()
            } else null
        }
        return null
    }

    private fun getContentLayoutResId(): Int {
        return if (isNeedRefreshLayout()) R.layout.base_layout_with_refresh else R.layout.base_layout
    }

    private fun isNeedRefreshLayout(): Boolean {
        return pageType == PageType.LIST
    }

    override fun setTitleLayoutLayout(layoutResId: Int) {
        mViewConfig?.titleLayoutId = layoutResId
    }

    override fun getContentView(): View? = mContentView

    override fun getViewState(): ViewState? = mCurrentState

    override fun getRefreshLayout(): SmartRefreshLayout? = smartRefreshLayout


    fun showState(
        viewState: ViewState,
        msg: String?
    ) {
        if (loadService == null) {
            L.e("loadService==null")
        }
        errorMessage = msg
        loadService?.showWithConvertor(viewState)
        mCurrentState = viewState
    }


    override fun getTitleView(
        isImmersionBarEnable: Boolean,
        isContentUnderTitleBar: Boolean
    ): ITitleView? {
        if (mTitleView == null) {
            setTitleLayout(isImmersionBarEnable, isContentUnderTitleBar)
        }
        return mTitleView as ITitleView?
    }
}