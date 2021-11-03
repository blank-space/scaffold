package com.dawn.sample.pkg.feature.view.search

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.ui.page.iface.ViewState
import com.dawn.base.ui.page.iface.ViewStateWithMsg
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivitySearchBinding
import com.dawn.sample.pkg.feature.viewmodel.SearchViewModel

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/3
 * @desc   :
 */
@Route(path = SamplePath.A_SEARCH_ACTIVITY)
class SearchActivity :
    BaseActivity<SearchViewModel, FeatureSamplePkgActivitySearchBinding>() {

    override fun initView() {
        getTitleView()?.setTitleText(TAG)
    }

    override fun initData() {
        setState(ViewStateWithMsg(state = ViewState.STATE_COMPLETED))
        mViewModel.uiState.observe(this@SearchActivity) {
            L.d("fetch data :$it")
        }
    }

    override fun initListener() {
        super.initListener()
        binding.etSearch.addTextChangedListener {
            val text = it.toString()
            mViewModel.queryParamterForNetWork(text)
        }

        mViewModel.searchResultMockNetWork.observe(this) {
            L.d("得到回调：$it")
        }

    }
}