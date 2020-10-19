package com.bsnl.base.imageloader.glide

import android.annotation.SuppressLint
import android.content.Context
import com.bsnl.base.imageloader.BaseImageLoaderStrategy
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
class GlideImageLoaderStrategy : BaseImageLoaderStrategy<ImageConfigImpl>, GlideAppliesOptions {

    @SuppressLint("CheckResult")
    override fun loadImage(ctx: Context, config: ImageConfigImpl) {

        requireNotNull(ctx, { "context is request" })
        requireNotNull(config, { "ImageConfigImpl is request" })
        requireNotNull(config.imageView, { "imageView is request" })

        val requests = GlideApp.with(ctx)
        var glideRequest = requests.load(config.url)
        when (config.cacheStrategy) {
            CacheStrategy.NONE -> glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE)
            CacheStrategy.RESOURCE -> glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            CacheStrategy.DATA -> glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA)
            CacheStrategy.AUTOMATIC -> glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            CacheStrategy.ALL -> glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
        }

        if (config.isCrossFade) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade())
        }
        if (config.isCenterCrop) {
            glideRequest.centerCrop()
        }

        if (config.isCircle) {
            glideRequest .transform( CircleCrop())
        }

        if (config.imageRadius > 0) {
            glideRequest.transform(RoundedCorners(config.imageRadius))
        }

        if (config.blurvarue > 0) {
            glideRequest.transform(BlurTransformation(config.blurvarue))
        }

        if (config.placeholder != 0) //设置占位符
        {
            glideRequest.placeholder(config.placeholder)
        }

        if (config.errorPic != 0) //设置错误的图片
        {
            glideRequest.error(config.errorPic)
        }

        if (config.fallback != 0) //设置请求 url 为空图片
        {
            glideRequest.fallback(config.fallback)
        }

        config.imageView?.let {
            glideRequest.into(it)
        }




    }

    override fun clear(ctx: Context, config: ImageConfigImpl) {
        requireNotNull(ctx, { "context is request" })
        requireNotNull(config, { "ImageConfigImpl is request" })
        requireNotNull(config.imageView, { "imageView is request" })


        if (config.imageView != null) {
            GlideApp.get(ctx).getRequestManagerRetriever().get(ctx).clear(config.imageView!!)
        }

        if (config.imageViews != null && config.imageViews!!.size > 0) { //取消在执行的任务并且释放资源
            for (imageView in config.imageViews!!) {
                GlideApp.get(ctx).getRequestManagerRetriever().get(ctx).clear(imageView)
            }
        }

        if (config.isClearDiskCache) { //清除本地缓存
            CoroutineScope(Dispatchers.IO).launch {
                Glide.get(ctx).clearDiskCache()
            }
        }

        if (config.isClearMemory) { //清除内存缓存
            CoroutineScope(Dispatchers.Main).launch {
                Glide.get(ctx).clearMemory()
            }

        }
    }

    override fun applyGlideOptions(context: Context, builder: GlideBuilder) {
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
    }
}