package com.bsnl.sample.pkg.feature.itemViewBinder

import coil.load
import coil.transform.CircleCropTransformation
import com.bsnl.base.log.L
import com.bsnl.common.page.base.BindingViewDelegate
import com.bsnl.sample.pkg.R
import com.bsnl.sample.pkg.databinding.FeatureSamplePkgRecycleItemCountdownBinding

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
            img.load("https://gank.io/images/d6bba8cf5b8c40f9ad229844475e9149") {
                crossfade(true)
                placeholder(R.drawable.ic_goods_placeholder)
                transformations(CircleCropTransformation())
            }
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