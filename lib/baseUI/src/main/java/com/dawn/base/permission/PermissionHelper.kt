package com.dawn.base.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dawn.base.utils.showToast
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.dialog.RationaleDialog
import com.permissionx.guolindev.request.PermissionBuilder
import java.lang.ref.WeakReference


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/12
 * @desc   : 权限申请
 */
object PermissionHelper {
    /**
     * @param activity 在Activity中申请，非必传
     * @param fragment 在Fragment中申请，非必传
     * @param permission 权限数组，必传
     * @param customDialog  自定义提示框，非必传
     * @param needExplainReason 是否要展示额外的提示语，非必传
     */
    fun request(
        activity: WeakReference<FragmentActivity>? = null,
        fragment: WeakReference<Fragment>? = null,
        permission: List<String>,
        customDialog: RationaleDialog? = null,
        needExplainReason: Boolean = false
    ) {
        var builder: PermissionBuilder? = null

        activity?.let {
            builder = PermissionX.init(it.get()).permissions(permission)
        }

        fragment?.let {
            builder = PermissionX.init(it.get()).permissions(permission)
        }

        requireNotNull(builder, { "Activity或者Fragment不能为空" })

        invokeRealRequest(builder!!, customDialog, needExplainReason)

    }

    private fun invokeRealRequest(
        builder: PermissionBuilder, customDialog: RationaleDialog? = null,
        needExplainReason: Boolean = false
    ) {
        if (needExplainReason) {
            builder.explainReasonBeforeRequest()
                .onExplainRequestReason { scope, deniedList, beforeRequest ->
                    if (customDialog != null) {
                        scope.showRequestReasonDialog(customDialog)
                    } else {
                        scope.showRequestReasonDialog(
                            deniedList,
                            "需要申请以下权限",
                            "确定"
                        )
                    }
                }

        }

        builder.onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                "请在系统设置中开启该权限",
                "确定"
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                "权限申请成功".showToast()
            } else {
                "以下权限被拒：$deniedList".showToast()
            }
        }
    }
}