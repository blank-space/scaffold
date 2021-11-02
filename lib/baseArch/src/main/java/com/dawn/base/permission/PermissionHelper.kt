package com.dawn.base.permission

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dawn.base.log.L
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.dialog.RationaleDialog
import com.permissionx.guolindev.request.PermissionBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import java.lang.ref.WeakReference

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/12
 * @desc   : 权限申请
 */
class PermissionHelper(
    var activity: WeakReference<FragmentActivity>? = null,
    var fragment: WeakReference<Fragment>? = null,
    externalScope: CoroutineScope,
    permission: List<String>,
    customDialog: RationaleDialog? = null,
    needExplainReason: Boolean = false
) {
    private var builder: PermissionBuilder? = null

    init {
        activity?.let {
            builder = PermissionX.init(it.get()).permissions(permission)
        }

        fragment?.let {
            builder = PermissionX.init(it.get()).permissions(permission)
        }
    }

    @ExperimentalCoroutinesApi
    fun getGrantedFlow(): Flow<Boolean> {
        return _permission
    }


    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    private val _permission = callbackFlow<Boolean> {
        if (needExplainReason) {
            builder?.explainReasonBeforeRequest()
                ?.onExplainRequestReason { scope, deniedList, beforeRequest ->
                    if (customDialog != null) {
                        scope.showRequestReasonDialog(customDialog!!)
                    } else {
                        scope.showRequestReasonDialog(
                            deniedList,
                            "需要申请以下权限",
                            "确定"
                        )
                    }
                }
        }
        builder?.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                "请在系统设置中开启该权限",
                "确定"
            )
        }?.request { allGranted, grantedList, deniedList ->
            trySend(allGranted)
        }
        awaitClose{
            L.e("clean up when Flow collection ends..")
        }
    }.shareIn(
        externalScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )


}