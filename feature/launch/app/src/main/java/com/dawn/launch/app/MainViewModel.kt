package com.dawn.launch.app


import androidx.lifecycle.liveData
import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.sample.pkg.feature.repository.IWanAndroidRepository
import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import okhttp3.RequestBody

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class MainViewModel : BaseViewModel() {
    private val repository by lazy { WanAndroidRepository }

    @ExperimentalCoroutinesApi
    fun test() = fetchDataWithoutState(repository.getBanners())

    fun login() = liveData {
        repository.login().collectLatest {
            emit(it.data)
        }
    }
}