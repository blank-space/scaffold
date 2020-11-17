package com.bsnl.common.page.delegate

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
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

import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   : Loading-Content-Empty-Error状态转换
 */
class WrapLayoutDelegateImpl(val mActivity: AppCompatActivity? = null,
                             val mFragment: Fragment? = null,
                             val mContentLayoutResID: Int = 0,
                             @RefreshType.Val val mRefreshType: Int = RefreshType.NONE,
                             val mOnViewStateListener: OnViewStateListener? = null) :
    WrapLayoutDelegate {
    //当前页面状态
    private var mCurrentState = ViewState.STATE_COMPLETED
    private var mContext: Context? = null
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

    //无数据功能按钮
    private var mEmptyBtn: TextView? = null


    init {
        if (mActivity != null) {
            mContext = mActivity
        } else {
            mContext = mFragment?.context
        }
        requireNotNull(mContext) {
            "context cannt be null"
        }
        mLayoutInflater = LayoutInflater.from(mContext)
        mViewConfig = ViewConfig()
    }

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
                val smartRefreshLayout: SmartRefreshLayout = mainView!!.findViewById<View>(R.id.content_wrap) as SmartRefreshLayout
                setupRefreshLayout(smartRefreshLayout)
            }
            mContentWrapView = mainView!!.findViewById(R.id.content_wrap)
        }
        return mView
    }

    private fun setupContentView() {
        val contentStub = mView!!.findViewById<View>(R.id.stub_content) as ViewStub
        if (contentStub != null) {
            contentStub.layoutResource = mContentLayoutResID
            mContentView = contentStub.inflate()
            val background = mContentView?.getBackground()
            if (background != null) {
                mContentView?.setBackground(null)
                mView!!.background = background
            }
        }
    }

    private fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout) {
        mRefreshLayout = RefreshLayoutProxy(smartRefreshLayout, object : OnRefreshAndLoadMoreListener {
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
                mRefreshLayout?.setEnableRefresh(false)
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
        setViewStubLayoutRes(R.id.stub_titlebar, mViewConfig?.titleLayoutId!!)
        mTitleView = inflateViewStub(R.id.stub_titlebar)
        layoutTitleView(isImmersionBarEnable, isContentUnderTitleBar, true)
        return mTitleView
    }


    fun layoutTitleView(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean, fromInit: Boolean) {
        if (mContentWrapView == null) {
            return
        }
        val mlp = mContentWrapView?.layoutParams as MarginLayoutParams
        if (isImmersionBarEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isContentUnderTitleBar) {
                mlp.topMargin = 44f.dp.toInt()+getStatusBarHeight(mContext!!)
            } else {
                mlp.topMargin = 0
                mTitleView!!.bringToFront()
            }
        } else {
            if (!isContentUnderTitleBar) {
                mlp.topMargin =44f.dp.toInt()
            } else {
                mlp.topMargin = 0
                mTitleView!!.bringToFront()
            }
        }
        if (!fromInit) {
            mContentWrapView!!.parent.requestLayout()
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    private fun setViewStubLayoutRes(@IdRes stubId: Int, @LayoutRes layoutResId: Int) {
        val viewStub = mView?.findViewById<View>(stubId) as ViewStub
        if (viewStub != null) {
            viewStub.layoutResource = layoutResId
        }
    }

    private fun inflateViewStub(@IdRes stubId: Int): View? {
        val viewStub = mView?.findViewById<View>(stubId) as ViewStub
        return if (viewStub != null && viewStub.layoutResource != 0) {
            viewStub.inflate()
        } else null
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

    override fun setEmptyText(text: Int) {
        mViewConfig?.emptyTxt = mContext?.getString(text)
        mEmptyTv?.setText(text)

    }

    override fun setEmptyText(text: String?) {
        mViewConfig?.emptyTxt = text
        mEmptyTv?.setText(text)
    }

    override fun setEmptyTextColor(color: Int) {
        mViewConfig?.emptyTxtColor = color
        mEmptyTv?.setTextColor(ContextCompat.getColor(mContext!!, color))
    }

    override fun setEmptyIcon(icon: Int) {
        mViewConfig?.emptyDrawableId = icon
        mEmptyImg?.setImageResource(icon)

    }

    override fun setEmptyBtnTxt(text: String?) {
        mViewConfig?.emptyBtnTxt = text
        mEmptyBtn?.setText(text)
        mEmptyBtn?.visibility = View.VISIBLE
    }


    private fun ensureEmptyView() {
        if (mEmptyView == null) {
            setViewStubLayoutRes(R.id.stub_empty, mViewConfig?.emptyLayoutId!!)
            mEmptyView = inflateViewStub(R.id.stub_empty)
            mEmptyImg = mEmptyView?.findViewById<View>(R.id.no_data_image) as ImageView
            mEmptyTv = mEmptyView?.findViewById<View>(R.id.no_data_txt) as TextView
            //mEmptyBtn = mEmptyView?.findViewById<View>(R.id.no_data_button) as TextView
            if (mEmptyImg != null && mViewConfig?.emptyDrawableId !== -1) {
                mEmptyImg?.setImageResource(mViewConfig?.emptyDrawableId!!)
            }
            if (mEmptyTv != null && !TextUtils.isEmpty(mViewConfig?.emptyTxt)) {
                mEmptyTv?.setText(mViewConfig?.emptyTxt)
            }
            if (mEmptyTv != null && mViewConfig?.emptyTxtColor !== 0) {
                mEmptyTv?.setTextColor(ContextCompat.getColor(mContext!!, mViewConfig?.emptyTxtColor!!))
            }
            if (mEmptyBtn != null) {
                if (!TextUtils.isEmpty(mViewConfig?.emptyBtnTxt)) {
                    mEmptyBtn?.setText(mViewConfig?.emptyBtnTxt)
                    mEmptyBtn?.setVisibility(View.VISIBLE)
                }
                mEmptyBtn?.setOnClickListener(View.OnClickListener { v -> mOnViewStateListener?.onNoDataBtnClick(v) })
            }
        }
    }

    private fun ensureLoadingView() {
        if (mLoadingView == null) {
            setViewStubLayoutRes(R.id.stub_loading, mViewConfig?.loadingLayoutId!!)
            mLoadingView = inflateViewStub(R.id.stub_loading)
        }
    }

    private fun ensureErrorView() {
        if (mErrorView == null) {
            setViewStubLayoutRes(R.id.stub_error, mViewConfig?.errorLayoutId!!)
            mErrorView = inflateViewStub(R.id.stub_error)
            mErrorView?.setOnClickListener { v -> mOnViewStateListener?.onReload(v) }
            mErrorImg = mErrorView?.findViewById<View>(R.id.imgv_reload) as ImageView
            mErrorTv = mErrorView?.findViewById<View>(R.id.tv_reload) as TextView
            mRetryTv = mErrorView?.findViewById<View>(R.id.tv_retry) as TextView
            if (mRetryTv != null) {
                mRetryTv!!.setOnClickListener { v -> mOnViewStateListener?.onReload(v) }
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


    fun showState(viewState: ViewState, show: Boolean, hideOther: Boolean, vararg args: Any?) {
        L.d("viewState:$viewState")
        setRefreshLayoutState(viewState)
        if (hideOther) {
            setContentViewVisible((ViewState.STATE_COMPLETED === viewState) and show) //加载完成
            setLoadingViewVisible((ViewState.STATE_LOADING === viewState) and show) //加载中
            setErrorViewVisible((ViewState.STATE_ERROR === viewState) and show, args) //加载失败
            setEmptyViewVisible((ViewState.STATE_EMPTY === viewState) and show, args) //空数据

        } else {
            when (viewState) {
                ViewState.STATE_COMPLETED -> setContentViewVisible(show)
                ViewState.STATE_LOADING -> setLoadingViewVisible(show)
                ViewState.STATE_ERROR -> setErrorViewVisible(show, args)
                ViewState.STATE_EMPTY -> setEmptyViewVisible(show, args)
            }
        }
        mCurrentState = viewState
    }


    private fun setEmptyViewVisible(visible: Boolean, args: Array<out Any?>) {
        if (visible && mEmptyView == null) {
            ensureEmptyView()
        }
        if (mEmptyView != null) {
            if (args != null) {
                //setEmptyArgs(args);
                for (arg in args) {
                    if (arg is Int) {
                        val resId = arg
                        val typeName = mContext!!.resources.getResourceTypeName(resId)
                        if ("string" == typeName) {
                            mEmptyTv!!.setText(resId)
                        }
                    } else if (arg is CharSequence) {
                        mEmptyTv!!.text = arg
                    }
                }
            }
            mEmptyView!!.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun setErrorViewVisible(visible: Boolean, args: Array<out Any?>) {
        if (visible && mErrorView == null) {
            ensureErrorView()
        }
        if (mErrorView != null) {
            setErrorArgs(*args)
            mErrorView!!.visibility = if (visible) View.VISIBLE else View.GONE
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
            if (arg is Int) {
                val resId = arg
                val typeName = mContext!!.resources.getResourceTypeName(resId)
                if ("string" == typeName) {
                    mErrorTv!!.setText(resId)
                } else {
                    mErrorImg?.setImageResource(resId)
                }
            } else if (arg is CharSequence) {
                mErrorTv!!.text = arg
            }
        }
    }

    private fun setLoadingViewVisible(visible: Boolean) {
        if (visible && mLoadingView == null) {
            ensureLoadingView()
        }

        mLoadingView?.post { mLoadingView!!.visibility = if (visible) View.VISIBLE else View.GONE }

    }

    private fun setContentViewVisible(visible: Boolean) {
        if (visible && mContentView == null) {
            mContentView = inflateViewStub(R.id.stub_content)
        }
        mContentView?.visibility = if (visible) View.VISIBLE else View.GONE
    }


    override fun getTitleView(isImmersionBarEnable: Boolean, isContentUnderTitleBar: Boolean): ITitleView? {
        if (mTitleView == null) {
            setTitleLayout(isImmersionBarEnable, isContentUnderTitleBar)
        }
        return mTitleView as ITitleView?
    }
}