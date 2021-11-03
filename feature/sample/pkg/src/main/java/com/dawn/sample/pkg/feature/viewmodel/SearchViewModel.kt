package com.dawn.sample.pkg.feature.viewmodel

import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dawn.base.log.L
import com.dawn.base.viewmodel.base.BaseViewModel
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.repository.PokemonNetwork
import com.dawn.sample.pkg.feature.repository.impl.PokeRepositoryImpl
import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/3
 * @desc   :
 */
class SearchViewModel : BaseViewModel() {
    private val _stateFlow = UnPeekLiveData<String>()
    val stateFlow: ProtectedUnPeekLiveData<String> = _stateFlow

    fun queryParamterForNetWork(paramter: String) {
        _stateFlow.value = paramter
    }
    private val _uiState = UnPeekLiveData<List<Article>>()
    val uiState: ProtectedUnPeekLiveData<List<Article>> = _uiState

    val testFlow = MutableStateFlow<String>("")

    init {
        viewModelScope.launch {
            WanAndroidRepository().getTopArticles().collect {
                _uiState.value = it.data
            }
        }
    }

    private val repository by lazy {
        WanAndroidRepository()
    }


    val searchResultMockNetWork = stateFlow.asFlow()
        .debounce(200)
        .filter { result ->
            L.d("input text:$result")
            return@filter !result.isEmpty()
        }
        .flatMapLatest { // 只显示最后一次搜索的结果，忽略之前的请求
            PokeRepositoryImpl(PokemonNetwork).mockSearch()
        }
        .catch { throwable ->
            //  异常捕获
        }
        .asLiveData()

    fun getSomeData(){
        viewModelScope.launch {
            repository.getTopArticles().collect {
                _uiState.value = it.data
            }
        }
    }
}