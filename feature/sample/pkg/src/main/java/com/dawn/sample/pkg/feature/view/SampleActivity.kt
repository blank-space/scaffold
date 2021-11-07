package com.dawn.sample.pkg.feature.view

import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.base.ui.page.base.SimpleListActivity
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.feature.itemViewBinder.ArticleItemViewBinder
import com.dawn.sample.pkg.feature.viewmodel.SampleViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
@Route(path = SamplePath.A_LISTVIEW_ACTIVITY)
class SampleActivity : SimpleListActivity<SampleViewModel>() {

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.apply {
            register(ArticleItemViewBinder())
            setHasStableIds(true)
        }
    }

    override fun initView() {
        super.initView()
        getTitleView()?.setTitleText(TAG)
    }
}