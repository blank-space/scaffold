package com.bsnl.common.ui.viewpager

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bsnl.base.log.L

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/10
 * @desc   :
 */
class CustomerViewpager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?
    ) : super(context, attrs)

    fun setInitFragmentListener(lsn: OnInitFragmentListener, fm: FragmentManager, behavior: Int) {
        onInitFragmentListener = lsn
        setOffscreenPageLimit(lsn.getTabs().size)
        initAdapter(fm, behavior)
    }

    private fun initAdapter(fm: FragmentManager, behavior: Int) {
        adapter = MyViewPagerAdapter(fm, behavior)
    }

    private var onInitFragmentListener: OnInitFragmentListener? = null

    interface OnInitFragmentListener {
        fun getFragment(position: Int): Fragment

        fun getTabs(): Array<String>

    }

    private val fragmentList: SparseArray<Fragment>? = null

    inner class MyViewPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {

        override fun getItem(position: Int): Fragment {
            //L.d("getItem=$position")
            onInitFragmentListener?.let {
                return it.getFragment(position)
            }
            return Fragment()
        }

        override fun getCount(): Int {
            onInitFragmentListener?.let {
                return it.getTabs().size
            }
            return 1
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position)
            fragmentList?.put(position, fragment as Fragment)
            return fragment
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            //L.d("destroyItem=$position")
            fragmentList?.remove(position)
            super.destroyItem(container, position, `object`)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            onInitFragmentListener?.let {
                return it.getTabs()[position]
            }
            return super.getPageTitle(position)
        }

    }
}