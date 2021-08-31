package com.bsnl.sample.pkg.feature.data.model

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/19
 * @desc   :
 */


data class PokeItemModel(
    var page: Int = 0,
    val name: String,
    val url: String
) {
    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://gank.io/images/d6bba8cf5b8c40f9ad229844475e9149"
    }

    override fun toString(): String {
        return "ListingData(name='$name', url='$url')"
    }
}