package com.bsnl.databinding

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bsnl.base.manager.KeyboardStateManager
import com.bsnl.base.manager.NetworkStateManager
import com.bsnl.common.iface.ITitleView
import com.bsnl.common.iface.ITrack
import com.bsnl.common.iface.IViewState
import com.bsnl.common.iface.ViewState
import com.bsnl.common.ui.viewStatus.Gloading
import com.bsnl.common.viewmodel.BaseViewModel
import com.jaeger.library.StatusBarUtil
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.ref.WeakReference


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class DataBindingActivity<T : BaseViewModel> : AppCompatActivity(), ITrack, IViewState {
    // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露
    private var mBinding: ViewDataBinding? = null
    var mTitleView: ITitleView? = null
    lateinit var mViewModel: T
    val TAG by lazy { javaClass.simpleName }
    private var msg: String? = null

    lateinit var mContext: Context
    var mActivity: WeakReference<Activity>? = null

    protected var mHolder: Gloading.Holder? = null


    /**
     * make a Gloading.Holder wrap with current activity by default
     * override this method in subclass to do special initialization
     *
    <li>    Gloading specialGloading = Gloading.from(new SpecialAdapter());      </li>
    <li>    mHolder = specialGloading.wrap(this).withRetry(new Runnable() {      </li>
    <li>        @Override                                                        </li>
    <li>        public void run() {                                              </li>
    <li>            onLoadRetry();                                               </li>
    <li>        }                                                                </li>
    <li>    });                                                                  </li>
     *
     */
    protected open fun initLoadingStatusViewIfNeed() {
        if (mHolder == null) {
            //bind status view to activity root view by default
            mHolder = Gloading.default?.wrap(this)?.withRetry(Runnable { onLoadRetry() })
        }
    }

    protected fun onLoadRetry() {
        initData()
    }

    open fun showLoading() {
        initLoadingStatusViewIfNeed()
        mHolder!!.showLoading()
    }

    open fun showLoadSuccess() {
        initLoadingStatusViewIfNeed()
        mHolder!!.showLoadSuccess()
    }

    open fun showLoadFailed() {
        initLoadingStatusViewIfNeed()
        mHolder!!.showLoadFailed()
    }

    open fun showEmpty() {
        initLoadingStatusViewIfNeed()
        mHolder!!.showEmpty()
    }


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

        val dataBindingConfig = initBindingConfig(getLayoutId())
        if (dataBindingConfig != null) {
            val binding: ViewDataBinding =
                DataBindingUtil.setContentView(this, dataBindingConfig.layout)
            binding.lifecycleOwner = this
            binding.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.viewModel)
            val bindingParams = dataBindingConfig.getBindingParams()
            bindingParams?.let {
                for (i in 0 until bindingParams.size()) {
                    binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
                }
            }
            mBinding = binding
        } else {
            if (getLayout() != null) {
                setContentView(getLayout())
            } else {
                setContentView(getLayoutId())
            }
        }

        initView()
        initListener()
        initData()
        setStatusBar()

    }


    fun initTitle(title: String) {
        mTitleView?.setTitleText(title)
    }

    /**
     * 不使用dataBinding时可用
     */
    protected open fun getLayout(): View? {
        return null
    }

    protected open fun getIntentData() {

    }


    protected open fun getRefreshLayout(): SmartRefreshLayout? {
        return null
    }


    protected open fun initListener() {
        mTitleView?.setNavIconOnClickListener(View.OnClickListener {
            finish()
        })

        mViewModel.viewState.observe(this, Observer
        {
            msg = it.msg
            it.state?.let { it1 -> setState(it1) }
        })
    }

    open fun setStatusBar() {
        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setTransparent(this)
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
                showEmpty()
            }

            ViewState.STATE_ERROR -> {
                showLoadFailed()

            }

            ViewState.STATE_SHOW_LOADING_DIALOG -> {
                showLoading()
            }
            ViewState.STATE_IDLE -> {
                //showLoadSuccess()

            }

        }

    }

    abstract fun initView()

    abstract fun getLayoutId(): Int

    /**
     * @return 返回null 表示不使用dataBinding
     */
    abstract fun initBindingConfig(layoutId: Int): DataBindingConfig?

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