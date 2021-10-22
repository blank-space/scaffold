package com.dawn.faster.state

import com.dawn.faster.utils.DispatcherLog
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object TaskState {
    @Volatile
    private var sCurrentSituation = ""
    private val sBeans: MutableList<TaskStateBean> =
        ArrayList()
    private var sTaskDoneCount =
        AtomicInteger()
    private const val sOpenLaunchStat = false // 是否开启统计
    var currentSituation: String
        get() = sCurrentSituation
        set(currentSituation) {
            if (!sOpenLaunchStat) {
                return
            }
            DispatcherLog.i("currentSituation   $currentSituation")
            sCurrentSituation = currentSituation
            setLaunchStat()
        }

    fun markTaskDone() {
        sTaskDoneCount.getAndIncrement()
    }

    fun setLaunchStat() {
        val bean = TaskStateBean()
        bean.situation = sCurrentSituation
        bean.count = sTaskDoneCount.get()
        sBeans.add(bean)
        sTaskDoneCount =
            AtomicInteger(0)
    }
}