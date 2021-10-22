package com.dawn.base.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * @author : LeeZhaoXing
 * @date : 2020/6/17
 * @desc :
 */
object RecyclerViewUtil {

    @SuppressLint("NotifyDataSetChanged")
    fun initRecyclerView(recyclerView: RecyclerView?, adapter: RecyclerView.Adapter<*>?) {
        requireNotNull(recyclerView, { "recyclerView cannot null" })
        requireNotNull(adapter, { "adapter cannot null" })
        recyclerView.apply {
            if (layoutManager == null) {
                layoutManager = LinearLayoutManager(recyclerView.context)
            }
            setHasFixedSize(true)
            this.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}