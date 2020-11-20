package com.bsnl.sample.pkg.feature.itemViewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsnl.base.utils.load
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.databinding.FeatureSamplePkgRecycleItemPokemonBinding
import com.bsnl.sample.pkg.feature.data.model.PokeItemModel
import com.drakeet.multitype.ItemViewBinder

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class PokemonItemViewBinder( val title: String = "") :
    ItemViewBinder<PokeItemModel, MyHolder>() {
    override fun onBindViewHolder(holder: MyHolder, item: PokeItemModel) {
        holder.bindData(item, holder.adapterPosition)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): MyHolder {
        return MyHolder((inflater.inflate(R.layout.feature_sample_pkg_recycle_item_pokemon, parent, false)))
    }

    override fun getItemId(item: PokeItemModel): Long {
        return item.hashCode().toLong()
    }
}

class MyHolder(view: View) : com.bsnl.databinding.viewHolder.BaseViewHolder<PokeItemModel>(view) {
    private val binding: FeatureSamplePkgRecycleItemPokemonBinding by viewHolderBinding(view)

    override fun bindData(data: PokeItemModel, position: Int) {
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