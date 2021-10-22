package com.dawn.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.ImageLoader
import coil.load
import coil.request.Disposable
import coil.request.ImageRequest
import coil.transform.Transformation

/**
 * @author : LeeZhaoXing
 * @date   : 2021/10/21
 * @desc   :
 */
fun ImageView.load(
    url: String,
    @DrawableRes drawableResId: Int,
    transformations: Transformation? = null
) {
    this.load(url) {
        placeholder(drawableResId)
        if (transformations != null) {
            transformations(transformations)
        }
    }
}

fun ImageView.load(
    uri: Uri,
    @DrawableRes drawableResId: Int,
    transformations: Transformation? = null
) {
    this.load(uri) {
        placeholder(drawableResId)
        if (transformations != null) {
            transformations(transformations)
        }
    }
}

fun getDrawable(url: String, context: Context, loadBitmapListener: ILoadDrawableListener? = null) {
    val imageLoader = ImageLoader(context)
    var disposable: Disposable? = null
    val request = ImageRequest.Builder(context)
        .data(url)
        .target { result ->
            loadBitmapListener?.onGetDrawable(result)
            disposable?.dispose()
        }
        .build()
    disposable = imageLoader.enqueue(request)
}