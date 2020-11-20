package com.bsnl.sample.pkg.feature.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
@Entity
data class PokeItemEntity(
    var page: Int = 0,
    @PrimaryKey
    val name: String,
    val url: String
) {
    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://pokeres.bastionbot.org/images/pokemon/$index.png"
    }

    override fun toString(): String {
        return "ListingData(name='$name', url='$url')"
    }
}