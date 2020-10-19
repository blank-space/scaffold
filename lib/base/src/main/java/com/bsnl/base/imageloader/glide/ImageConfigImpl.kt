package com.bsnl.base.imageloader.glide

import android.graphics.Bitmap
import android.widget.ImageView
import com.bsnl.base.imageloader.ImageConfig
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
class ImageConfigImpl : ImageConfig() {

    @CacheStrategy.Strategy
    var cacheStrategy =
        0 //0 对应DiskCacheStrategy.all,1对应DiskCacheStrategy.NONE,2对应DiskCacheStrategy.SOURCE,3对应DiskCacheStrategy.RESULT
    var fallback = 0 //请求 url 为空,则使用此图片作为占位符
    var imageRadius = 0  //图片每个圆角的大小
    var blurvarue = 0  //高斯模糊值, 值越大模糊效果越大
    var imageViews: Array<ImageView>? = null
    var isCrossFade = false //是否使用淡入淡出过渡动画
    var isCenterCrop = false //是否将图片剪切为 CenterCrop
    var isCircle = false  //是否将图片剪切为圆形
    var isClearMemory = false  //清理内存缓存
    var isClearDiskCache = false  //清理本地缓存




}