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
    val request = ImageRequest.Builder(context).apply {
        target(this@load)
        error(drawableResId)
        placeholder(drawableResId)
    }
    if (url.isEmpty()) {
        request.data(null)
        XImageLoader.imageLoader.enqueue(request.build())
        return
    }
    request.data(url)
    if (transformations != null) {
        request.transformations(transformations)
    }
    XImageLoader.imageLoader.enqueue(request.build())

}

fun ImageView.loadUri(
    uri: Uri,
    @DrawableRes drawableResId: Int,
    transformations: Transformation? = null
) {
    val request = ImageRequest.Builder(context)
        .data(uri).placeholder(drawableResId)
        .target(this)

    if (transformations != null) {
        request.transformations(transformations)
    }
    XImageLoader.imageLoader.enqueue(request.build())
}

fun getDrawable(url: String, context: Context, loadBitmapListener: ILoadDrawableListener? = null) {
    var disposable: Disposable? = null
    val request = ImageRequest.Builder(context)
        .data(url)
        .target { result ->
            loadBitmapListener?.onGetDrawable(result)
            disposable?.dispose()
        }
        .build()
    disposable = XImageLoader.imageLoader.enqueue(request)
}
