package com.bsnl.sample.pkg.feature.view

import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bsnl.base.utils.showToast
import com.bsnl.common.iface.OnItemClickListener
import com.bsnl.common.page.base.BaseListActivity
import com.bsnl.common.utils.RecyclerViewUtil
import com.bsnl.common.utils.getVm
import com.bsnl.common.utils.startActivity
import com.bsnl.sample.export.path.SamplePath
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.itemViewBinder.PokemonItemViewBinder
import com.bsnl.sample.pkg.feature.viewmodel.SampleViewModel
import com.drakeet.multitype.MultiTypeAdapter

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
@Route(path = SamplePath.A_LISTVIEW_ACTIVITY)
class SampleActivity : BaseListActivity<SampleViewModel>() {

    companion object {
        fun actionStart(context: Context) {
            startActivity<SampleActivity>(context)
        }
    }

    private lateinit var itemView: View

    override fun registerItem(adapter: MultiTypeAdapter?) {
        adapter?.apply {
            register(PokemonItemViewBinder())
            //setHasStableIds(true)
        }
    }

    override fun initView() {
        super.initView()
        getTitleView()?.setTitleText(TAG)
    }

    override fun initViewModel(): SampleViewModel = getVm()

    override fun initListener() {
        super.initListener()
        RecyclerViewUtil.setOnItemClickListener(getRecyclerView(), object : OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                FirstActivity.actionStart(this@SampleActivity)
            }

            override fun onChildClick(v: View, position: Int) {
                v.id.toString().showToast()
                FirstActivity.actionStart(this@SampleActivity)
            }
        }, R.id.name, R.id.avator)
    }


}