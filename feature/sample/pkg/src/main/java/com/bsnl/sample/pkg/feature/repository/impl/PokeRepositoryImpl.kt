package com.bsnl.sample.pkg.feature.repository.impl


import androidx.room.withTransaction
import com.bsnl.base.log.L
import com.bsnl.common.constant.DEFAULT_PAGE_SIZE
import com.bsnl.sample.pkg.feature.data.dao.AppDataBase
import com.bsnl.sample.pkg.feature.data.entity.PokeItemEntity
import com.bsnl.sample.pkg.feature.data.model.PokemonListResponse
import com.bsnl.sample.pkg.feature.data.model.PokeItemModel
import com.bsnl.sample.pkg.feature.mapper.Mapper
import com.bsnl.sample.pkg.feature.repository.IPokeRepository
import com.bsnl.sample.pkg.feature.repository.PokemonNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
class PokeRepositoryImpl(
    val network: PokemonNetwork,
    val db: AppDataBase,
) : IPokeRepository {
    var localPage=1;

    /**
     * 尝试做数据转换
     */
    override fun fetchPokemonList(pageNo: Int): Flow<PokemonListResponse> {

        return flow {
            var response: PokemonListResponse? = null
            var model = mutableListOf<PokeItemModel>()
            val dao = db.pokeDao()
            //先判断DB上是否有需要的数据，若无，从服务器请求，并缓存到DB
            if (dao.getPokemonList(pageNo).isNullOrEmpty()) {
                response = network.fetchPokemonList(pageNo)
                model.addAll(response.results)
                model?.apply {
                    //网络数据->本地数据
                    val item = map {
                        PokeItemEntity(
                            page = pageNo,
                            name = it.name,
                            url = it.url
                        )
                    }
                    db.withTransaction {
                        dao.insertPokemonList(item)
                    }
                }
            } else {
                response= PokemonListResponse(results = model)
                val allSize = dao.getPokemonAllList()
                if(allSize.size / DEFAULT_PAGE_SIZE >= localPage){
                    val item = dao.getPokemonList(pageNo).map {
                        PokeItemModel(
                            page = it.page,
                            name = it.name,
                            url = it.url
                        )
                    }
                    model.addAll(item)
                    ++localPage
                }
            }
            emit(response!!)
        }.flowOn(Dispatchers.IO)
    }


}