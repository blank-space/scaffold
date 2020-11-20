package com.bsnl.sample.pkg.feature.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bsnl.sample.pkg.feature.data.entity.PokeItemEntity
import com.bsnl.sample.pkg.feature.data.model.PokemonListResponse

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
@Database(
    entities = arrayOf(PokeItemEntity::class),
    version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun pokeDao(): PokeDao


}