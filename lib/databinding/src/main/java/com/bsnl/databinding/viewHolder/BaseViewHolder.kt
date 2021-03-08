package com.bsnl.databinding.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bsnl.base.log.L
import kotlin.jvm.Throws

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/20
 * @desc   :
 */
abstract class BaseViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {

    @Throws(Exception::class)
    abstract fun bindData(data: T, position: Int)

    fun view() = view

    fun getContext() = view.context

    inline fun <reified T : ViewDataBinding> viewHolderBinding(v: View): Lazy<T> = lazy {
        "cannot find the matched layout."
        requireNotNull(DataBindingUtil.bind<T>(v)) {
        }
    }

}