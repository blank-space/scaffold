package com.dawn.sample.pkg.feature.data.entity

import com.squareup.moshi.JsonClass

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/5
 * @desc   :
 */
@JsonClass(generateAdapter = true)
data class UserInfo(
    var admin: Boolean = false,
    var chapterTops: List<String>,
    var coinCount: Int,
    var collectIds: List<Int>,
    var email: String,
    var icon: String,
    var id: Int,
    var nickname: String,
    var password: String,
    var publicName: String,
    var token: String,
    var type: Int,
    var username: String
)