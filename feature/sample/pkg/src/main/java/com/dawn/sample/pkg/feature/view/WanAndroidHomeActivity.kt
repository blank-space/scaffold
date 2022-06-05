package com.dawn.sample.pkg.feature.view

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.base.ui.page.base.SimpleListActivity
import com.dawn.sample.export.api.ISampleService
import com.dawn.sample.export.path.SamplePath
import com.dawn.sample.pkg.feature.itemViewBinder.ArticleItemViewBinder
import com.dawn.sample.pkg.feature.viewmodel.SampleViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   : 玩安卓首页
 */
@Route(path = SamplePath.A_LISTVIEW_ACTIVITY)
class WanAndroidHomeActivity : SimpleListActivity<SampleViewModel>() {
    @Autowired
    @JvmField
    var sampleService: ISampleService? = null

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.apply {
            register(ArticleItemViewBinder(sampleService))
        }
    }

    override fun initView() {
        super.initView()
        getTitleView()?.setTitleText(TAG)
    }
}