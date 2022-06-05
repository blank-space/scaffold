package com.dawn.sample.pkg.feature.data.entity

/**
 * @author : LeeZhaoXing
 * @date   : 2021/11/5
 * @desc   :
 */

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