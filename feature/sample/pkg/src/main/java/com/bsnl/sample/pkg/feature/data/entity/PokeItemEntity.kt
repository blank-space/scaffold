package com.bsnl.sample.pkg.feature.data.entity


/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */
data class PokeItemEntity(
    var page: Int = 0,
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