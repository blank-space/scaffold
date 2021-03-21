package com.bsnl.sample.pkg.feature.view.databinding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

/**
 * @author : LeeZhaoXing
 * @date   : 2021/3/10
 * @desc   :
 */
object BindingAdapters {

    @BindingAdapter(value = ["numberOfSets"], requireAll = false)
    @JvmStatic
    fun setNumberOfSets(view: GoodsCounterView, value: Long) {
        view.setText(value.toString())
    }

    @InverseBindingAdapter(attribute = "numberOfSets", event = "numberOfSetsAttrChanged")
    @JvmStatic
    fun getNumberOfSets(view: GoodsCounterView): Long {
        val text = view.getText()
        return if (text.isEmpty()) 0 else text.toLong()
    }

    @BindingAdapter(value = ["numberOfSetsAttrChanged"], requireAll = false)
    @JvmStatic
    fun setListener(view: GoodsCounterView, listener: InverseBindingListener?) {
        view.listener = listener
    }
}