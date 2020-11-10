package com.bsnl.sample.pkg.feature.itemViewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsnl.base.log.L
import com.bsnl.base.utils.load
import com.bsnl.common.dataBinding.viewHolder.BaseViewHolder
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.databinding.FeatureSamplePkgRecycleItemPokemonBinding
import com.bsnl.sample.pkg.feature.model.ListingData
import com.bsnl.sample.pkg.feature.view.FirstActivity
import com.drakeet.multitype.ItemViewBinder

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class PokemonItemViewBinder(val title: String = "") : ItemViewBinder<ListingData, MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder, item: ListingData) {
        holder.bindData(item, holder.adapterPosition)
        L.e(" ${title} ,onBindViewHolder")
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): MyHolder {
        L.e(" onCreateViewHolder")
        return MyHolder(
            (inflater.inflate(
                R.layout.feature_sample_pkg_recycle_item_pokemon,
                parent,
                false
            ))
        )
    }

    override fun getItemId(item: ListingData): Long {
        return item.hashCode().toLong()
    }

}

class MyHolder(view: View) : BaseViewHolder<ListingData>(view) {
    private val binding: FeatureSamplePkgRecycleItemPokemonBinding by viewHolderBinding(view)


    override fun bindData(data: ListingData, position: Int) {
        binding.apply {
            pokemon = data
            executePendingBindings()
        }
        binding.avator.load {
            this.url = data.getImageUrl()
            this.fallback = R.color.colorAccent
        }
    }

}