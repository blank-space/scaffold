package com.dawn.sample.pkg.feature.mapper

import com.dawn.sample.pkg.feature.data.entity.PokeItemEntity
import com.dawn.sample.pkg.feature.data.model.PokeItemModel

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
class PokemonMapper : Mapper<PokeItemEntity, PokeItemModel> {

    override fun map(input: PokeItemEntity): PokeItemModel {
        return PokeItemModel(
            page = input.page,
            name = input.name,
            url = input.url
        )
    }
}