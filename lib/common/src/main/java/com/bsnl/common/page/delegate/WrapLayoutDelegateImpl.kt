package com.bsnl.common.page.delegate

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bsnl.base.dsl.dp
import com.bsnl.base.log.L
import com.bsnl.common.R
import com.bsnl.common.iface.*
import com.bsnl.common.page.delegate.iface.OnViewStateListener
import com.bsnl.common.page.delegate.iface.WrapLayoutDelegate
import com.bsnl.common.refreshLayout.RefreshLayoutProxy
import com.bsnl.common.viewmodel.RequestType
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView

import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   : Loading-Content-Empty-Error状态转换
 */
class WrapLayoutDelegateImpl(
    val mActivity: AppCompatActivity? = null,
    val mFragment: Fragment? = null,
    val mContentLayoutResID: Int = 0,
    val childView: View? = null,
    @RefreshType.Val val mRefreshType: Int = RefreshType.NONE,
    var mOnViewStateListener: OnViewStateListener? = null,
    var showImgHeader: Boolean = false
) : WrapLayoutDelegate {
    //当前页面状态
    private var mCurrentState = ViewState.STATE_COMPLETED
    private var mContext: Context
    private var mRefreshLayout: RefreshLayoutProxy? = null
    private var mViewConfig: ViewConfig? = null

    //页面base View
    private var mView: View? = null
    private var mLayoutInflater: LayoutInflater? = null

    //内容布局
    private var mContentView: View? = null

    //加载中布局
    private var mLoadingView: View? = null

    //无数据布局
    private var mEmptyView: View? = null

    //加载失败布局
    private var mErrorView: View? = null

    //标题布局
    private var mTitleView: View? = null
    private var mContentWrapView: View? = null
    var mErrorImg: ImageView? = null
    var mErrorTv: TextView? = null
    var mRetryTv: TextView? = null

    // 无数据显示的图片
    private var mEmptyImg: ImageView? = null

    // 无数据提示文字
    private var mEmptyTv: TextView? = null

    // 说明文字
    private var mIllustrateTv: TextView? = null

    //无数据功能按钮
    private var mEmptyBtn: TextView? = null

    //底部布局
    private var mBottomView: View? = null

    //头部导航栏布局
    private var imgHeaderView: View? = null

    //完全自定义View
    private var mCustomView: View? = null

    private var smartRefreshLayout: SmartRefreshLayout? = null


    private var loadingPopup: LoadingPopupView? = null

    //loadingIcon without background
    // private var mLoadingIcon :AlertDialog?=null


    init {
        if (mActivity != null) {
            mContext = mActivity
        } else {
            mContext = mFragment?.requireContext()!!
        }
        requireNotNull(mContext) {
            "context cannt be null"
        }
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
                setupRefreshLayout(smartRefreshLayout!!)
                mContentWrapView = mainView.findViewById(R.id.fl_content)
            } else {
                mContentWrapView = mainView?.findViewById(R.id.content_wrap)
            }
            if (mViewConfig?.bottomLayoutId!! > -1) {
                ensureBottomView()
            }

            if (childView != null && mContentWrapView != null) {
                addChildViewToBaseRootView(mContentWrapView as ViewGroup)
            }
        }
        return mView
    }

    fun addChildViewToBaseRootView(parentViewGroup: ViewGroup) {
        parentViewGroup.addView(childView)
    }

    private fun setupContentView() {
        val contentStub = mView?.findViewById<View>(R.id.stub_content) as ViewStub
        if (mContentLayoutResID != 0) {
            contentStub.layoutResource = mContentLayoutResID
            mContentView = contentStub.inflate()
        } else {
            mContentView = childView
        }

        val background = mContentView?.getBackground()
        if (background != null) {
            mContentView?.setBackground(null)
            mView?.background = background
        }
    }

    private fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout) {
        if (bottomViewHeight > 0) {
            val mlp = smartRefreshLayout.layoutParams as MarginLayoutParams
            mlp.bottomMargin = bottomViewHeight
        }
        mRefreshLayout =
            RefreshLayoutProxy(smartRefreshLayout, object : OnRefreshAndLoadMoreListener {
                override fun onRefresh(refreshLayout: IRefreshLayout?) {
                    mOnViewStateListener?.onRefresh(refreshLayout)
                }

                override fun onLoadMore(refreshLayout: IRefreshLayout?) {
                    mOnViewStateListener?.onLoadMore(refreshLayout)
                }

            })
        processRefreshType(mRefreshType)

    }

    private fun setRefreshLayoutState(viewState: ViewState) {
        if (viewState === ViewState.STATE_COMPLETED) {
            processRefreshType(mRefreshType)
        } else {
            if (mRefreshLayout != null) {
                mRefreshLayout?.setEnableRefresh(viewState == ViewState.STATE_EMPTY)
                mRefreshLayout?.setEnableLoadMore(false)
            }
        }
    }

    private fun processRefreshType(@RefreshType.Val refreshType: Int) {
        if (mRefreshLayout == null) {
            return
        }
        when (refreshType) {
            RefreshType.REFRESH_AND_LOAD_MORE -> {
                mRefreshLayout?.setEnableRefresh(true)
                mRefreshLayout?.setEnableLoadMore(true)
            }
            RefreshType.REFRESH_ONLY -> {
                mRefreshLayout?.setEnableRefresh(true)
                mRefreshLayout?.setEnableLoadMore(false)
            }
            RefreshType.LOAD_MORE_ONLY -> {
                mRefreshLayout?.setEnableRefresh(false)
                mRefreshLayout?.setEnableLoadMore(true)
            }
            else -> {
                mRefreshLayout?.setEnableRefresh(false)
                mRefreshLayout?.setEnableLoadMore(false)
            }
        }
    }

    fun setTitleLayout(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): View? {
        if (mTitleView != null) {
            return mTitleView
        }
        mViewConfig?.titleLayoutId?.let { setViewStubLayoutRes(R.id.stub_titlebar, it) }
        mTitleView = inflateViewStub(R.id.stub_titlebar)
        layoutTitleView(isImmersionBarEnable, isContentUnderTitleBar, true)
        return mTitleView
    }


    fun layoutTitleView(
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
        if (isImmersionBarEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isContentUnderTitleBar) {
                mlp.topMargin = 0
                smartLayoutLp?.topMargin = 0
                titleLp.topMargin = getStatusBarHeight()

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
        return if (isNeedRefreshLayout()) R.layout.lib_common_base_layout_with_refresh else R.layout.lib_common_base_layout
    }

    private fun isNeedRefreshLayout(): Boolean {
        return mRefreshType != RefreshType.NONE
    }

    override fun setTitleLayoutLayout(layoutResId: Int) {
        mViewConfig?.titleLayoutId = layoutResId
    }

    override fun setEmptyLayout(layoutResId: Int) {
        mViewConfig?.emptyLayoutId = layoutResId
    }

    override fun setLoadingLayout(layoutResId: Int) {
        mViewConfig?.loadingLayoutId = layoutResId
    }

    override fun setErrorLayout(layoutResId: Int) {
        mViewConfig?.errorLayoutId = layoutResId
    }

    private var bottomViewHeight: Int = 0

    override fun setBottomLayout(layoutResId: Int, height: Int) {
        mViewConfig?.bottomLayoutId = layoutResId
        bottomViewHeight = height
    }

    override fun setCustomLayout(layoutResId: Int) {
        mViewConfig?.customLayoutId = layoutResId
    }

    override fun setEmptyText(text: Int) {
        mViewConfig?.emptyTxt = mContext.getString(text)
        mEmptyTv?.setText(text)

    }

    override fun setEmptyText(text: String?) {
        mViewConfig?.emptyTxt = text
        mEmptyTv?.setText(text)
    }

    override fun setIllustrateText(text: Int) {
        mViewConfig?.illustrateTxt = mContext.getString(text)
        mIllustrateTv?.setText(text)
    }

    override fun setIllustrateText(text: String?) {
        mViewConfig?.illustrateTxt = text
        mIllustrateTv?.setText(text)
    }

    override fun setRetryText(text: String?) {
        mViewConfig?.retryBtnTxt = text
        mRetryTv?.setText(text)
    }

    override fun setRetryText(text: Int) {
        mViewConfig?.retryBtnTxt = mContext.getString(text)
        mRetryTv?.setText(text)
    }

    override fun setEmptyTextColor(color: Int) {
        mViewConfig?.emptyTxtColor = color
        mEmptyTv?.setTextColor(ContextCompat.getColor(mContext, color))
    }

    override fun setEmptyIcon(icon: Int) {
        mViewConfig?.emptyDrawableId = icon
        mEmptyImg?.setImageResource(icon)

    }

    override fun setErrorIcon(icon: Int) {
        mViewConfig?.errorDrawableId = icon
        mErrorImg?.setImageResource(icon)
    }


    override fun setEmptyBtnTxt(text: String?) {
        mViewConfig?.emptyBtnTxt = text
        mEmptyBtn?.setText(text)
        mEmptyBtn?.visibility = View.VISIBLE
    }


    private fun ensureEmptyView() {
        if (mEmptyView == null) {
            mViewConfig?.emptyLayoutId?.let { setViewStubLayoutRes(R.id.stub_empty, it) }
            mEmptyView = inflateViewStub(R.id.stub_empty)
            mEmptyImg = mEmptyView?.findViewById<View>(R.id.no_data_image) as? ImageView
            mEmptyTv = mEmptyView?.findViewById<View>(R.id.no_data_txt) as? TextView
            mEmptyBtn = mEmptyView?.findViewById<View>(R.id.tv_nav) as? TextView

            if (mEmptyImg != null && mViewConfig?.emptyDrawableId !== -1) {
                mEmptyImg?.setImageResource(mViewConfig?.emptyDrawableId!!)
            }
            if (mEmptyTv != null && !TextUtils.isEmpty(mViewConfig?.emptyTxt)) {
                mEmptyTv?.setText(mViewConfig?.emptyTxt)
            }
            if (mEmptyTv != null && mViewConfig?.emptyTxtColor !== 0) {
                mEmptyTv?.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        mViewConfig?.emptyTxtColor!!
                    )
                )
            }
            if (mEmptyBtn != null) {
                if (!TextUtils.isEmpty(mViewConfig?.emptyBtnTxt)) {
                    mEmptyBtn?.setText(mViewConfig?.emptyBtnTxt)
                    mEmptyBtn?.setVisibility(View.VISIBLE)
                }
                mEmptyBtn?.setOnClickListener { v ->
                    mOnViewStateListener?.onNoDataBtnClick(v)
                }
            }

            mIllustrateTv = mEmptyView?.findViewById<View>(R.id.tv_illustrate) as TextView
            if (mIllustrateTv != null && !TextUtils.isEmpty(mViewConfig?.illustrateTxt)) {
                mIllustrateTv?.setText(mViewConfig?.illustrateTxt)
            }
        }
    }

    private fun ensureLoadingView() {
        if (mLoadingView == null) {
            mViewConfig?.loadingLayoutId?.let { setViewStubLayoutRes(R.id.stub_loading, it) }
            mLoadingView = inflateViewStub(R.id.stub_loading)
        }
    }

    private fun ensureCustomView() {
        if (mCustomView == null) {
            mViewConfig?.customLayoutId?.let { setViewStubLayoutRes(R.id.stub_custom, it) }
            mCustomView = inflateViewStub(R.id.stub_custom)
            mOnViewStateListener?.onLoadCustomLayout(mCustomView)
        }
    }

    private fun ensureBottomView() {
        if (mBottomView == null) {
            mViewConfig?.bottomLayoutId?.let { setViewStubLayoutRes(R.id.stub_bottom, it) }
            mBottomView = inflateViewStub(R.id.stub_bottom)
        }
    }


    private fun ensureErrorView() {
        if (mErrorView == null) {
            mViewConfig?.errorLayoutId?.let { setViewStubLayoutRes(R.id.stub_error, it) }
            mErrorView = inflateViewStub(R.id.stub_error)

            mErrorImg = mErrorView?.findViewById<View>(R.id.imgv_reload) as? ImageView
            if (mErrorImg != null && mViewConfig?.errorDrawableId !== -1) {
                mErrorImg?.setImageResource(mViewConfig?.errorDrawableId!!)
            }
            mErrorTv = mErrorView?.findViewById<View>(R.id.tv_reload) as? TextView
            mRetryTv = mErrorView?.findViewById<View>(R.id.tv_retry) as? TextView
            mIllustrateTv = mErrorView?.findViewById<View>(R.id.tv_illustrate) as? TextView
            if (mRetryTv != null) {
                if (!TextUtils.isEmpty(mViewConfig?.retryBtnTxt)) {
                    mRetryTv?.setText(mViewConfig?.retryBtnTxt)
                }
                mRetryTv?.setOnClickListener { v -> mOnViewStateListener?.onReload(v) }
            }
            if (mIllustrateTv != null && !TextUtils.isEmpty(mViewConfig?.illustrateTxt)) {
                mIllustrateTv?.setText(mViewConfig?.illustrateTxt)
            }
        }
    }

    override fun getEmptyView(): View? {
        ensureEmptyView()
        return mEmptyView
    }

    override fun getErrorView(): View? {
        ensureErrorView()
        return mErrorView
    }

    override fun getContentView(): View? = mContentView

    override fun getLoadingView(): View? {
        ensureLoadingView()
        return mLoadingView
    }

    override fun getViewState(): ViewState? = mCurrentState

    override fun getRefreshLayout(): RefreshLayoutProxy? = mRefreshLayout


    fun showState(
        viewState: ViewState,
        show: Boolean,
        showBottomViewWhenEmpty: Boolean,
        hideOther: Boolean,
        vararg args: Any?
    ) {
        setRefreshLayoutState(viewState)
        //L.d("wrap showState:$viewState ,show:$show,hideOther:$hideOther")
        if (hideOther) {
            //加载完成
            setContentViewVisible((ViewState.STATE_COMPLETED === viewState || ViewState.STATE_LOGIN === viewState) and show)
            //加载中
            setLoadingViewVisible((ViewState.STATE_LOADING === viewState || ViewState.STATE_SHOW_LOADING_DIALOG === viewState) and show)
            //加载失败
            setErrorViewVisible(
                (ViewState.STATE_ERROR === viewState || ViewState.STATE_NETWORK_ERROR === viewState) and show,
                args
            )
            //空数据
            setEmptyViewVisible((ViewState.STATE_EMPTY === viewState) and show, args)
            //底部Layout
            setBottomViewVisible(ViewState.STATE_COMPLETED === viewState || showBottomViewWhenEmpty)
            //完全自定义Layout
            setCustomViewVisible(ViewState.STATE_CUSTOM === viewState)

        } else {
            when (viewState) {
                ViewState.STATE_COMPLETED -> setContentViewVisible(show)
                ViewState.STATE_LOADING, ViewState.STATE_SHOW_LOADING_DIALOG -> setLoadingViewVisible(
                    show
                )
                ViewState.STATE_ERROR, ViewState.STATE_NETWORK_ERROR -> setErrorViewVisible(
                    show,
                    args
                )
                ViewState.STATE_EMPTY -> setEmptyViewVisible(show, args)
                else -> {
                    throw IllegalArgumentException("没有这个状态值")
                }
            }
        }
        mCurrentState = viewState
    }

    private fun setCustomViewVisible(visible: Boolean) {
        if (visible && mCustomView == null) {
            ensureCustomView()
        }
        mCustomView?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setBottomViewVisible(visible: Boolean) {
        if (visible && mBottomView == null) {
            ensureBottomView()
        }
        mBottomView?.visibility = if (visible) View.VISIBLE else View.GONE

    }

    private fun setEmptyViewVisible(visible: Boolean, args: Array<out Any?>) {
        if (visible && mEmptyView == null) {
            ensureEmptyView()
        }
        if (mEmptyView != null) {
            for (arg in args) {
                //Int类型对应mIllustrateTv，String类型对应mEmptyTv
                if (arg is Int) {
                    val resId = arg
                    if (resId <= 0) return
                    val typeName = mContext.resources.getResourceTypeName(resId)
                    if ("string" == typeName) {
                        mIllustrateTv?.setText(resId)
                    }
                } else if (arg is CharSequence) {
                    mEmptyTv?.text = arg
                }
            }
            mEmptyView?.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun setErrorViewVisible(visible: Boolean, args: Array<out Any?>) {
        if (visible && mErrorView == null) {
            ensureErrorView()
        }
        if (mErrorView != null) {
            setErrorArgs(*args)
            mErrorView?.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    fun setErrorArgs(vararg args: Any?) {
        if (mErrorImg == null) {
            ensureEmptyView()
        }
        if (mErrorImg == null || mErrorTv == null) {
            return
        }
        for (arg in args) {
            //Int类型对应mIllustrateTv，String类型对应mEmptyTv
            if (arg is Int) {
                val resId = arg
                if (resId <= 0) return
                val typeName = mContext.resources.getResourceTypeName(resId)
                if ("string" == typeName) {
                    mIllustrateTv?.setText(resId)
                }
            } else if (arg is CharSequence) {
                mErrorTv?.text = arg
            }
        }
    }


    private fun setLoadingIconVisible(visible: Boolean) {
        /* if (mLoadingIcon == null) {
             mLoadingIcon = CommonDialogBuilder(mContext, DialogStyleConfig.LOADING_ONLY_ICON)
                 .setCanceledOnTouchOutside(true)
                 .setCancelable(true).build(isShow = false)!!
         }
         mLoadingIcon?.apply {
             if (visible) show() else dismiss()
         }*/
        //暂时无区别

    }

    private fun setLoadingViewVisible(visible: Boolean) {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(mContext)
                .dismissOnBackPressed(false)
                .asLoading("加载中")
                .show() as LoadingPopupView
        } else {
            if (visible) {
                loadingPopup!!.show()
            } else {
                loadingPopup!!.dismiss()
            }
        }
    }

    private fun setContentViewVisible(visible: Boolean) {
        if (visible && mContentView == null) {
            mContentView = inflateViewStub(R.id.stub_content)
        }
        mContentView?.visibility = if (visible) View.VISIBLE else View.GONE
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

    override fun getBottomLayout(): View? {
        return mBottomView
    }


}