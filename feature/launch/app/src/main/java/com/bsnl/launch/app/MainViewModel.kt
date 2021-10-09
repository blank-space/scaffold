package com.bsnl.launch.app

import com.bsnl.common.viewmodel.BaseViewModel
import com.bsnl.sample.pkg.feature.repository.IWanAndroidRepository
import com.bsnl.sample.pkg.feature.repository.impl.WanAndroidRepository

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