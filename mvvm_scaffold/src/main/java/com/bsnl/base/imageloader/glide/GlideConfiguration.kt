package com.bsnl.base.imageloader.glide

import android.content.Context
import com.bsnl.base.BaseApp
import com.bsnl.base.imageloader.ImageLoader
import com.bsnl.base.imageloader.glide.okhttp.OkHttpUrlLoader
import com.bsnl.base.net.ServiceCreator
import com.bsnl.base.utils.FileUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import java.io.File
import java.io.InputStream

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
@GlideModule
class GlideConfiguration : AppGlideModule() {
    val IMAGE_DISK_CACHE_MAX_SIZE = 500 * 1024 * 1024 //图片缓存文件最大值为500Mb
    val cacheFilePath by lazy {
        BaseApp.application.getExternalCacheDir()
            .toString() + File.separator + "imageCache" + File.separator
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        builder.setDiskCache {
            DiskLruCacheWrapper.create(
                FileUtil.makeDirs(File(cacheFilePath, "glide")), IMAGE_DISK_CACHE_MAX_SIZE.toLong()
            )
        }
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize
        val customMemoryCacheSize = (1.2 * defaultMemoryCacheSize).toInt()
        val customBitmapPoolSize = (1.2 * defaultBitmapPoolSize).toInt()
        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize.toLong()))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize.toLong()))

        //将配置 Glide 的机会转交给 GlideImageLoaderStrategy,如你觉得框架提供的 GlideImageLoaderStrategy
        //并不能满足自己的需求,想自定义 BaseImageLoaderStrategy,那请你最好实现 GlideAppliesOptions
        //因为只有成为 GlideAppliesOptions 的实现类,这里才能调用 applyGlideOptions(),让你具有配置 Glide 的权利
        val loadImgStrategy = ImageLoader.loadImgStrategy
        if (loadImgStrategy != null && loadImgStrategy is GlideAppliesOptions) {
            (loadImgStrategy as GlideAppliesOptions).applyGlideOptions(context, builder)
        }
    }


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //Glide 默认使用 HttpURLConnection 做网络请求,在这切换成 Okhttp 请求
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java, OkHttpUrlLoader.Factory(ServiceCreator.getClient())
        )
        val loadImgStrategy = ImageLoader.loadImgStrategy
        if (loadImgStrategy != null && loadImgStrategy is GlideAppliesOptions) {
            (loadImgStrategy as GlideAppliesOptions).registerComponents(context, glide, registry)
        }
    }


    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}