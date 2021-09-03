package com.bsnl.common.page.base

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bsnl.base.BaseApp
import com.bsnl.common.utils.inflateBindingWithGeneric
import com.bsnl.common.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType


/**
 * @author : LeeZhaoXing
 * @date   : 2020/8/17
 * @desc   :
 */
abstract class BaseBindingActivity<T : BaseViewModel, VB : ViewBinding> : BaseActivity<T>() {
    override fun getLayout(): View {
        val binding: VB = inflateBindingWithGeneric(layoutInflater)
        return binding.root
    }

    override fun initViewModel(): T {
        val persistentClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(BaseApp.application)).get(persistentClass)
        return mViewModel
    }

}