package com.dawn.sample.pkg.feature.data.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
@JsonClass(generateAdapter = true)
data class Banner(
    val id: Int = 0,
    val desc: String,
    val imagePath: String,
    val title: String
)
