package com.dawn.sample.pkg.feature.interceptor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.dawn.base.log.L
import com.dawn.base.permission.PermissionHelper
import com.dawn.base.utils.ActivitysManager
import com.dawn.base.utils.GlobalHandler
import com.dawn.sample.export.path.SamplePath
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/2
 * @desc   :
 */
@Interceptor(name = "checkPermission", priority = 6)
class PermissionInterceptor : IInterceptor {
    private var ctx: Context? = null
    override fun init(context: Context?) {
        println("PermissionInterceptor init")
        ctx = context
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val path = postcard?.path
        if (path == SamplePath.A_FIND_LOCATION_ACTIVITY && !isLocationPermissionGranted(ctx!!)) {
            L.d("process PermissionInterceptor，thread:${Thread.currentThread().name}")
            val aty = WeakReference(ActivitysManager.getTopActivity() as FragmentActivity)
            GlobalHandler.post {
                aty.get()?.let {
                    it.lifecycleScope.let { it1 ->
                        it1.launch {
                            PermissionHelper(
                                activity = aty,
                                permission = listOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ),
                                externalScope = it1
                            ).getGrantedFlow()
                                .flowWithLifecycle(it.lifecycle, Lifecycle.State.RESUMED)
                                .collect { isBoolean ->
                                    if (isBoolean) {
                                        callback?.onContinue(postcard)
                                    } else {
                                        callback?.onInterrupt(null)
                                    }
                                    aty.clear()
                                }
                        }
                    }
                }
            }
        } else {
            callback?.onContinue(postcard)
        }
    }


    fun isLocationPermissionGranted(context: Context): Boolean {
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