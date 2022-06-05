package com.dawn.sample.pkg.feature.viewmodel

import com.dawn.base.DataResult
import com.dawn.base.BaseListBean
import com.dawn.base.viewmodel.base.BaseListViewModel
import com.dawn.base.viewmodel.iface.RequestType
import com.dawn.sample.pkg.feature.data.entity.Article
import com.dawn.sample.pkg.feature.repository.impl.WanAndroidRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/21
 * @desc   :
 */
class SampleViewModel : BaseListViewModel() {

    private val repository by lazy {
        WanAndroidRepository
    }
    private var hasAddTopList = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getList(): Flow<DataResult<out Any>?>? {
        return flow {
            val topList = repository.getTopArticles()
            val itemList = repository.getIndexList(pageNo)
            val list = mutableListOf<Article>()
            val data = DataResult<BaseListBean<Article>>()

            if (mRequestType == RequestType.REFRESH) {
                hasAddTopList = false
            }

            if (!hasAddTopList) {
                hasAddTopList = true
                topList.collectLatest {
                    list.addAll(it.data)
                }
            }
            itemList.collectLatest {
                list.addAll(it.data.datas)
                data.data = BaseListBean(list, it.data.total)
                data.data.results
                data.code = "000000"
                data.msg = "ok"
            }
            emit(data)
        }
    }


}