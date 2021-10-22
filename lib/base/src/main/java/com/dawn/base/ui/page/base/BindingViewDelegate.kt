package com.dawn.base.ui.page.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dawn.base.utils.inflateBindingWithGeneric
import com.drakeet.multitype.ItemViewDelegate

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/3
 * @desc   : 简化ViewHolder的创建
 */
abstract class BindingViewDelegate<T, VB : ViewBinding> : ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        BindingViewHolder(inflateBindingWithGeneric<VB>(parent))

    class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
