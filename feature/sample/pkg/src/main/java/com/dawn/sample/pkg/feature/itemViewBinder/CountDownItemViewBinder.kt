package com.dawn.sample.pkg.feature.itemViewBinder

import coil.load
import coil.transform.CircleCropTransformation
import com.dawn.base.imageLoader.load
import com.dawn.base.ui.page.base.BindingViewDelegate
import com.dawn.sample.pkg.R
import com.dawn.sample.pkg.databinding.FeatureSamplePkgRecycleItemCountdownBinding

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/2
 * @desc   :
 */
class CountDownItemViewBinder : BindingViewDelegate<Long, FeatureSamplePkgRecycleItemCountdownBinding>() {

    override fun onBindViewHolder(
        holder: BindingViewHolder<FeatureSamplePkgRecycleItemCountdownBinding>,
        item: Long,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, item)
        } else {
            updateText(holder, item)
        }
    }

    override fun onBindViewHolder(
        holder: BindingViewHolder<FeatureSamplePkgRecycleItemCountdownBinding>,
        item: Long
    ) {
        updateText(holder, item)
        holder.binding.apply {
            img.load("https://image-upyun.halvie.com/images/20210611/1623414322613.jpg!/format/webp",R.drawable.ic_goods_placeholder,CircleCropTransformation())
        }
    }

    private fun updateText(
        holder: BindingViewHolder<FeatureSamplePkgRecycleItemCountdownBinding>,
        item: Long) {
        holder.binding.apply {
            if (item > 0) {
                tvTitle.text = "${item / 1000}s"
            } else {
                tvTitle.text = "over"
            }
        }
    }
}