package com.bsnl.common.page.delegate

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bsnl.base.log.L
import com.bsnl.base.utils.showToast
import com.bsnl.common.R
import com.bsnl.common.callback.ErrorLayoutCallback
import com.bsnl.common.iface.*
import com.bsnl.common.page.delegate.iface.OnViewStateListener
import com.bsnl.common.page.delegate.iface.IWrapLayoutDelegate
import com.bsnl.common.refreshLayout.RefreshLayoutProxy
import com.bsnl.common.utils.dp
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/18
 * @desc   :
 */
class WrapLayoutDelegateImpl(
    val mActivity: AppCompatActivity? = null,
    val mFragment: Fragment? = null,
    val childView: View? = null,
    @RefreshType.Val val mRefreshType: Int = RefreshType.NONE,
    var mOnViewStateListener: OnViewStateListener? = null,
    var loadService: LoadService<*>? = null,
    var useLoadService: Boolean = true
) : IWrapLayoutDelegate {
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

    //标题布局
    private var mTitleView: View? = null
    private var mContentWrapView: View? = null
    private var smartRefreshLayout: SmartRefreshLayout? = null

    init {
        mContext = if (mActivity != null) {
            mActivity
        } else {
            mFragment?.requireContext()!!
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

            }
            mContentWrapView = mainView?.findViewById(R.id.fl_content)
            if (childView != null && mContentWrapView != null) {
                addChildViewToBaseRootView(mContentWrapView as ViewGroup)
            }

            if (loadService == null && useLoadService) {
                loadService = childView?.let {
                    LoadSir.getDefault().register(it) {
                        "reload".showToast()
                    }
                }
            }
        }
        return mView
    }



    private fun addChildViewToBaseRootView(parentViewGroup: ViewGroup) {
        parentViewGroup.addView(childView)
    }

    private fun setupContentView() {
        mContentView = childView

        val background = mContentView?.background
        if (background != null) {
            mContentView?.background = null
            mView?.background = background
        }
    }

    private fun setupRefreshLayout(smartRefreshLayout: SmartRefreshLayout) {
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
        mViewConfig?.titleLayoutId?.let { setViewStubLayoutRes(R.id.stub_title, it) }
        mTitleView = inflateViewStub(R.id.stub_title)
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
        return if (isNeedRefreshLayout()) R.layout.common_base_layout_with_refresh else R.layout.common_base_layout
    }

    private fun isNeedRefreshLayout(): Boolean {
        return mRefreshType != RefreshType.NONE
    }

    override fun setTitleLayoutLayout(layoutResId: Int) {
        mViewConfig?.titleLayoutId = layoutResId
    }

    override fun getContentView(): View? = mContentView

    override fun getViewState(): ViewState? = mCurrentState

    override fun getRefreshLayout(): RefreshLayoutProxy? = mRefreshLayout


    fun showState(
        viewState: ViewState,
        show: Boolean,
        hideOther: Boolean,
        vararg args: Any?
    ) {
        setRefreshLayoutState(viewState)
        L.d("wrap showState:$viewState ,show:$show,hideOther:$hideOther")
        if (hideOther) {
            //加载完成
            setContentViewVisible((ViewState.STATE_COMPLETED === viewState || ViewState.STATE_LOGIN === viewState) and show)
            setErrorViewVisible((ViewState.STATE_ERROR === viewState) and show)
        } else {
            when (viewState) {
                ViewState.STATE_COMPLETED -> setContentViewVisible(show)
            }
        }
        mCurrentState = viewState
    }

    private fun setErrorViewVisible(visible: Boolean) {
        if (visible) {
            loadService?.showCallback(ErrorLayoutCallback::class.java)
        }
    }

    private fun setContentViewVisible(visible: Boolean) {
        if (visible) {
            loadService?.showSuccess()
        }
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