package com.bsnl.sample.pkg.feature.view.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bsnl.sample.pkg.feature.view.FirstFragment

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/7
 * @desc   :
 */
class TabAdapter(frag: FragmentActivity,val fragmentList:List<Fragment>) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemId(position: Int): Long {
        return fragmentList[position].hashCode().toLong()
    }
}