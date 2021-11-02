package com.dawn.sample.pkg.feature.view.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.BaseBindingActivity
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.base.utils.onClick
import com.dawn.base.utils.startActivity
import com.dawn.base.viewmodel.StubViewModel
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityLocationBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/2
 * @desc   :
 */
@Route(path = SamplePath.A_FIND_LOCATION_ACTIVITY)
class FindLocationActivity : BaseBindingActivity<StubViewModel, FeatureSamplePkgActivityLocationBinding>() {


    companion object {
        fun startAction(context: Context) {
            startActivity<FindLocationActivity>(context)
        }
    }

    override fun isNeedInjectARouter()= true

    override fun initView() {
        setTitle("FindLocationActivity")
    }

    override fun initData() {
        setState(ViewStateWithMsg(state = ViewState.STATE_LOADING))
    }

    @ExperimentalCoroutinesApi
    override fun initListener() {
        super.initListener()
        binding.tvFindLocation.onClick = {
            mActivity?.get()?.let { it1 -> requestLocationWhenOnStart(it1) }
        }
    }


    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    private fun requestLocationWhenOnStart(context: Context) {
        lifecycleScope.launch {
            if (isLocationPermissionGranted(context)) {
                NetWorkLocationHelper(mContext, lifecycleScope)
                    .getNetLocationFlow()
                    .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                    .collect { location -> L.d("最新的位置是：$location") }
            }
        }
    }


    private fun isLocationPermissionGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED)
    }


}