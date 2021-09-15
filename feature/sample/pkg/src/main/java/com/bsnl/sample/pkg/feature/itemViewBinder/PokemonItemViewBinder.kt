package com.bsnl.sample.pkg.feature.itemViewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bsnl.base.log.L
import com.bsnl.common.utils.dp
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.feature.data.model.PokeItemModel
import com.drakeet.multitype.ItemViewBinder

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class PokemonItemViewBinder(private val mView: View? = null) :
    ItemViewBinder<PokeItemModel, PokemonItemViewBinder.MyHolder>() {
    private val dp4 = 4f.dp
    override fun onBindViewHolder(holder: MyHolder, item: PokeItemModel) {
        if (mView != null) {
            holder.itemView.setLayoutParams(
                RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            )
        }

        holder.tvName.text = item.name
        holder.ivAvatar.load(item.getImageUrl()) {
            crossfade(true)
            //圆形
            //transformations(CircleCropTransformation())
            //圆角
            transformations(RoundedCornersTransformation(dp4))
        }
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): MyHolder {
        if (mView == null) {
            return MyHolder(
                (inflater.inflate(
                    R.layout.feature_sample_pkg_recycle_item_pokemon,
                    parent,
                    false
                ))
            )
        } else {
            if (mView.parent != null) {
                val prt = mView.parent as ViewGroup
                prt.removeView(mView)
            }
            val holder = MyHolder(mView)
            if (holder.itemView.parent != null) {
                val prt = holder.itemView.parent as ViewGroup
                prt.removeView(holder.itemView)
            }
            L.d("onCreateViewHolder :${holder.layoutPosition}")
            return holder
        }
    }


    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.avator)
        val tvName = view.findViewById<TextView>(R.id.name)
    }
}

