package com.dawn.base.ui.page.base

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dawn.base.utils.inflateBindingWithGeneric
import com.drakeet.multitype.ItemViewDelegate
import java.util.HashSet
import java.util.LinkedHashSet

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/3
 * @desc   : 简化ViewHolder的创建
 */
abstract class BindingViewDelegate<T, VB : ViewBinding> : ItemViewDelegate<T, BindingViewDelegate.BindingViewHolder<VB>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        BindingViewHolder(inflateBindingWithGeneric<VB>(parent))

    class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root){
        private val mViews: SparseArray<View?> = SparseArray()

        private val childClickViewIds: LinkedHashSet<Int?> = LinkedHashSet()

        fun clearViews() {
            mViews.clear()
        }

        fun <V : View?> getView(@IdRes viewId: Int): V? {
            var view = mViews[viewId]
            if (view == null) {
                view = itemView.findViewById(viewId)
                mViews.put(viewId, view)
            }
            return view as V?
        }

        fun getChildClickViewIds(): HashSet<Int?> {
            return this.childClickViewIds
        }

        fun addOnChildClickListener(viewId: Int):BindingViewHolder<VB> {
            childClickViewIds.add(viewId)
            return this
        }

        fun removeOnClick(viewId: Int):BindingViewHolder<VB> {
            childClickViewIds.remove(viewId)
            return this
        }
    }



}
