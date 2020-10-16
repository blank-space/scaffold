package com.bsnl.base.utils

import android.app.Activity

/**
 * @author : LeeZhaoXing
 * @date   : 2020/5/13
 * @desc   : Activity队列管理
 */
object ActivitysManager {

    private val activities =ArrayList<Activity>()

    fun addActivity(activity: Activity){
        activities.add(activity)
    }

    fun removeActivity(activity: Activity){
        activities.remove(activity)
    }

    fun finishAll(){
        for (activity in activities){
            if(!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}