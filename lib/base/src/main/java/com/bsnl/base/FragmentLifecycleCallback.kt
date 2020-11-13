package com.bsnl.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/13
 * @desc   :
 */
class FragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks()  {

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        super.onFragmentCreated(fm, f, savedInstanceState)
        Timber.v("${f::class.java.name}#onFragmentCreated ")
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        Timber.v("${f::class.java.name}#onFragmentViewCreated ")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentDestroyed(fm, f)
        Timber.v("${f::class.java.name}#onFragmentDestroyed ")
    }


    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        super.onFragmentViewDestroyed(fm, f)
        Timber.v("${f::class.java.name}#onFragmentViewDestroyed ")
    }
}