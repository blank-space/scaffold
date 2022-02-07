package com.dawn.base.ui.page.base

import com.dawn.base.databinding.BaseRecycerviewBinding
import com.dawn.base.ui.page.delegate.iface.PageType
import com.dawn.base.viewmodel.base.BaseListViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 最简单的LitView-Fragment,如果布局比较复杂，可以使用[BaseFragment]
 *
 */
abstract class SimpleListFragment<T : BaseListViewModel> : BaseListFragment<T, BaseRecycerviewBinding>(){
    override fun getPageType(): Int {
        return PageType.LIST
    }
}