package com.dawn.sample.pkg.feature.view

import android.content.Context
import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.dawn.base.ui.page.base.SimpleListActivity
import com.dawn.base.utils.ItemClickSupport
import com.dawn.base.utils.startActivity
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

    companion object {
        fun actionStart(context: Context) {
            startActivity<SampleActivity>(context)
        }
    }


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


    override fun initListener() {
        super.initListener()

    }

}