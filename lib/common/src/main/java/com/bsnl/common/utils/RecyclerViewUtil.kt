package com.bsnl.common.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bsnl.base.log.L


/**
 * @author : LeeZhaoXing
 * @date : 2020/6/17
 * @desc :
 */
object RecyclerViewUtil {

    fun initRecyclerView(recyclerView: RecyclerView?, adapter: RecyclerView.Adapter<*>?) {

        requireNotNull(recyclerView, { "recyclerView cannot null" })
        requireNotNull(adapter, { "adapter cannot null" })

        recyclerView.also {
            (it.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
            if (it.layoutManager == null) {
                it.layoutManager = LinearLayoutManager(recyclerView.context)
            }
            it.setHasFixedSize(true)
            it.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }
}