package com.bsnl.base

import android.R
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.style.TtsSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.bsnl.base.log.L
import com.bsnl.base.utils.ActivitysManager
import com.bsnl.base.widget.ShowFps
import timber.log.Timber

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class ActivityLifecycleCallback:Application.ActivityLifecycleCallbacks, FragmentManager.FragmentLifecycleCallbacks() {
    private val mParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.v("${activity::class.java.name}#onActivityCreated ")
        ActivitysManager.addActivity(activity)
        (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(this, false);
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStarted(activity: Activity) {
        L.v("${activity::class.java.name}#onActivityStarted ")
    }


    override fun onActivityResumed(activity: Activity) {
        L.v("${activity::class.java.name}#onActivityResumed ")
        if(BuildConfig.LOG_DEBUG) {
            (activity.findViewById<View>(R.id.content) as ViewGroup).addView(
                ShowFps.instance,
                mParams
            )
        }
    }

    override fun onActivityPaused(activity: Activity) {
        L.v("${activity::class.java.name}#onActivityPaused ")
        if(BuildConfig.LOG_DEBUG) {
            (activity.findViewById<View>(R.id.content) as ViewGroup).removeView(ShowFps.instance)
        }

    }

    override fun onActivityStopped(activity: Activity) {
        L.v("${activity::class.java.name}#onActivityStopped ")

    }

    override fun onActivityDestroyed(activity: Activity) {
        L.v("${activity::class.java.name}#onActivityDestroyed ")
        ActivitysManager.removeActivity(activity)
        (activity as? FragmentActivity)?.supportFragmentManager?.unregisterFragmentLifecycleCallbacks(this);
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        L.v("${f::class.java.name}#onFragmentCreated ")
        super.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        L.v("${f::class.java.name}#onFragmentViewDestroyed ")
        super.onFragmentViewDestroyed(fm, f)
    }



}