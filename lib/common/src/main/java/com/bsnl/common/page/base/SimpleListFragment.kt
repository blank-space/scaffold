package com.bsnl.common.page.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bsnl.common.databinding.CommonRecycerviewBinding
import com.bsnl.common.iface.IRefreshLayout
import com.bsnl.common.iface.RefreshType
import com.bsnl.common.page.delegate.ListViewDelegateImpl
import com.bsnl.common.page.delegate.iface.IListViewDelegate
import com.bsnl.common.viewmodel.BaseListViewModel
import com.bsnl.common.viewmodel.RequestType
import com.drakeet.multitype.MultiTypeAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.common_recycerview.*

/**
 * @author : LeeZhaoXing
 * @date   : 2020/9/16
 * @desc   : 最简单的LitView-Fragment,如果布局比较复杂，可以使用[BaseBindingFragment]
 *
 */
abstract class SimpleListFragment<T : BaseListViewModel> : BaseListFragment<T, CommonRecycerviewBinding>() {

}