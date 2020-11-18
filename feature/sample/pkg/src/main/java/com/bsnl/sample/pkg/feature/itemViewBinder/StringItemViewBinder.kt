package com.bsnl.sample.pkg.feature.itemViewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bsnl.databinding.viewHolder.BaseViewHolder
import com.bsnl.sample.pkg.R
import com.drakeet.multitype.ItemViewBinder

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class StringItemViewBinder() : ItemViewBinder<String, StringItemViewBinder.TextHolder>() {
    override fun onBindViewHolder(holder: TextHolder, item: String) {
        holder.title.text = item

    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): TextHolder {
        return TextHolder(
            (inflater.inflate(R.layout.feature_sample_pkg_recycle_item_string, parent, false))
        )
    }

    override fun getItemId(item: String): Long {
        return item.hashCode().toLong()
    }


    inner class TextHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById<TextView>(R.id.tv_title)
    }
}