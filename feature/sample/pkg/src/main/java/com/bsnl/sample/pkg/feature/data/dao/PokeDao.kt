package com.bsnl.sample.pkg.feature.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bsnl.sample.pkg.feature.data.entity.PokeItemEntity

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokeItemEntity>)

    @Query("SELECT * FROM PokeItemEntity WHERE page = :pageNo")
    suspend fun getPokemonList(pageNo: Int): List<PokeItemEntity>

    @Query("SELECT * FROM PokeItemEntity")
    suspend fun getPokemonAllList(): List<PokeItemEntity>
}