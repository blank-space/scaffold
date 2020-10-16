package com.bsnl.base.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bsnl.base.imageloader.ILoadBitmapListener
import com.bsnl.base.imageloader.ImageLoader
import com.bsnl.base.imageloader.glide.BlurTransformation
import com.bsnl.base.imageloader.glide.ImageConfigImpl
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/12
 * @desc   : 图片加载工具
 */

private var loadBitmapListener: ILoadBitmapListener? = null

private val target = object : SimpleTarget<Bitmap>() {
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        loadBitmapListener?.let {
            it.onGetBitmap(resource)
        }
    }
}

fun ImageView.load(config: ImageConfigImpl.() -> Unit) {
    ImageLoader.loadImage(
        this.context,
        ImageConfigImpl()
            .apply { imageView = this@load }
            .also(config)
    )
}

/**
 * 使用该方法，需要调用[clearLoadBitmapListener],释放引用
 */
fun getBitmapByGlide(configImpl: ImageConfigImpl, callback: ILoadBitmapListener) {
    val options = RequestOptions()
    configImpl?.let {
        if (it.isCenterCrop) {
            options.centerCrop()
        }
        if (it.isCircle) {
            options.transform(CircleCrop())
        }
        if (it.imageRadius > 0) {
            options.transform(RoundedCorners(it.imageRadius))
        }
        if (it.blurvarue > 0) {
            options.transform(BlurTransformation(it.blurvarue))
        }
        if (it.placeholder != 0) {
            options.placeholder(it.placeholder)
        }
        if (it.errorPic != 0) {
            options.error(it.errorPic)
        }
        if (it.fallback != 0) {
            options.fallback(it.fallback)
        }
    }
    loadBitmapListener = callback
    Glide.with(configImpl.imageView!!.context).asBitmap().load(configImpl.url).apply(options).into(target)

}

fun clearLoadBitmapListener(){
    loadBitmapListener=null
}



