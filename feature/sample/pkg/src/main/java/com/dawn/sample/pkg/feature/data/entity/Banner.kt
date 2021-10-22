package com.dawn.sample.pkg.feature.data.entity

import com.google.gson.annotations.SerializedName

/**
 * @author : LeeZhaoXing
 * @date   : 2021/9/15
 * @desc   :
 */
data class Banner(@SerializedName("id")
                  val id: Int = 0,
                  @SerializedName("desc")
                  val desc: String,
                  @SerializedName("imagePath")
                  val imagePath: String,
                  @SerializedName("title")
                  val title: String)
