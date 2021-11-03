package com.dawn.launch.app


import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.sample.pkg.feature.repository.IWanAndroidRepository
import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class MainViewModel : BaseViewModel() {
    val repository: IWanAndroidRepository by lazy { WanAndroidRepository() }

    fun test () =  fetchData(repository.getBanners()){

    }
}