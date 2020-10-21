package com.bsnl.common.dataBinding

import android.util.SparseArray
import androidx.lifecycle.ViewModel

class DataBindingConfig constructor(
        val layout: Int,
        val vmVariableId :Int,
        val viewModel: ViewModel

) {

    private val bindingParams: SparseArray<Any> = SparseArray()

    fun getBindingParams(): SparseArray<Any>? {
        return bindingParams
    }

    fun addBindingParam(variableId: Int, value: Any): DataBindingConfig {
        if (bindingParams[variableId] == null) {
            bindingParams.put(variableId, value)
        }
        return this
    }
}
