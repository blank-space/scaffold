package com.bsnl.common.dataBinding

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.bsnl.common.iface.ITrack
import com.bsnl.common.iface.IViewState
import com.bsnl.common.iface.ViewState
import com.bsnl.common.ui.viewStatus.Gloading
import com.bsnl.common.viewmodel.BaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class DataBindingFragment<T : BaseViewModel> : Fragment(), ITrack, IViewState {
    private val HANDLER = Handler()
    protected var mAnimationLoaded = false
    var mActivity: WeakReference<Activity>? = null
    private lateinit var mBinding: ViewDataBinding
    lateinit var mViewModel: T
    private val TAG by lazy { javaClass.simpleName }
    private var msg: String? = null

    protected var mHolder: Gloading.Holder? = null

    /**
     * make a Gloading.Holder wrap with current activity by default
     * override this method in subclass to do special initialization
     * <p>
    Gloading specialGloading = Gloading.from(new SpecialAdapter());
    mHolder = specialGloading.wrap(this).withRetry(new Runnable() {
    @Override
    public void run() {
    onLoadRetry();
    }
    });
     *</p>
     */

    protected fun onLoadRetry() {
        initData()
    }

    open fun showLoading() {
        mHolder?.let {
            it.showLoading()
        }
    }

    open fun showLoadSuccess() {
        mHolder?.let {
            it.showLoadSuccess()
        }
    }

    open fun showLoadFailed() {
        mHolder?.let {
            it.showLoadFailed()
        }
    }

    open fun showEmpty() {
        mHolder?.let {
            it.showEmpty()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = WeakReference(context as FragmentActivity)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = initViewModel()
        getIntentData()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBindingConfig = initBindingConfig(getLayoutId())

        if (dataBindingConfig != null) {
            val binding: ViewDataBinding =
                DataBindingUtil.inflate(inflater, dataBindingConfig.layout, container, false)
            binding.lifecycleOwner = this
            binding.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.viewModel)
            val bindingParams = dataBindingConfig.getBindingParams()
            bindingParams?.let {
                for (i in 0 until bindingParams.size()) {
                    binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
                }
            }

            mBinding = binding

            val parent = binding.root.getParent()
            if (parent != null) {
                parent as ViewGroup
                parent.removeView(binding.root)
            }

            if (isWrapped()) {
                mHolder =
                    Gloading.default?.wrap(mBinding?.root!!)?.withRetry(Runnable { onLoadRetry() })
                return mHolder?.wrapper
            } else {
                return mBinding?.root
            }
        } else {

            val view = getLayout() ?: inflater.inflate(getLayoutId(), null)

            val parent = view.getParent()
            if (parent != null) {
                parent as ViewGroup
                parent.removeView(view)
            }

            if (isWrapped()) {
                mHolder = Gloading.default?.wrap(view)?.withRetry(Runnable { onLoadRetry() })
                return mHolder?.wrapper
            } else {
                return view
            }
        }

    }

    /**
     * 默认使用Gloading
     */
    protected fun isWrapped(): Boolean {
        return true
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        // 错开动画转场与 UI 刷新的时机，避免掉帧卡顿的现象
        HANDLER.postDelayed(Runnable {
            if (!mAnimationLoaded) {
                mAnimationLoaded = true
                initView()
                initListener()
                initData()

            }
        }, 200)
        return super.onCreateAnimation(transit, enter, nextAnim)
    }


    abstract fun initView()


    abstract fun initData()

    abstract fun getLayoutId(): Int

    abstract fun initBindingConfig(layoutId: Int): DataBindingConfig

    abstract fun initViewModel(): T

    protected open fun getIntentData() {

    }

    /**
     * 不使用dataBinding时可用
     */
    protected open fun getLayout(): View? {
        return null
    }


    protected open fun initListener() {
        mViewModel.viewState.observe(requireActivity(), Observer {
            msg = it.msg
            setState(it.state)
        })

    }

    override fun setState(state: ViewState) {
        when (state) {
            ViewState.STATE_LOADING -> {
                //刷新有自带动画，不用显示LoadingCallback
                showLoading()
            }

            ViewState.STATE_COMPLETED -> {
                showLoadSuccess()
            }

            ViewState.STATE_EMPTY -> {
                hideLoadingDlg()
                showEmpty()
            }

            ViewState.STATE_ERROR -> {
                showLoadFailed()
            }

            ViewState.STATE_SHOW_LOADING_DIALOG -> {
                //加载更多有自带加载动画,不用显示loadingDialog
                showLoading()
            }

            ViewState.STATE_IDLE -> {
                showLoadSuccess()
            }
        }
    }

    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }

    protected fun showLoadingDlg() {
    }

    protected fun hideLoadingDlg() {
    }
}