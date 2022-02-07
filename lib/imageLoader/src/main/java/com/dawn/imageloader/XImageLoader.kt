package com.dawn.imageloader

import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.dawn.base.contentProvider.BaseArchContentProvider
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

/**
 * @author : LeeZhaoXing
 * @date   : 2022/1/21
 * @desc   :
 */
object XImageLoader {

    val imageLoader by lazy {
        ImageLoader.Builder(BaseArchContentProvider.ctx)
            .availableMemoryPercentage(0.2)
            .bitmapPoolPercentage(0.2)
            .crossfade(true)
            .okHttpClient {
                val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(BaseArchContentProvider.app))
                    .dispatcher(dispatcher)
                    .build()
            }
            .componentRegistry{
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(BaseArchContentProvider.app))
                } else {
                    add(GifDecoder())
                }
                add(SvgDecoder(BaseArchContentProvider.app))
            }.build()


    }
}