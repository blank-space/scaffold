package com.bsnl.base.imageloader

import android.content.Context

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   : 图片加载策略,实现  [BaseImageLoaderStrategy]
 * 并通过 [ImageLoaders] 配置后,才可进行图片请求
 */
interface BaseImageLoaderStrategy<in T> {


    /**
     * 加载图片
     *
     * @param ctx    [Context]
     * @param config 图片加载配置信息
     */
    fun loadImage(ctx: Context, config: T)

    /**
     * 停止加载
     *
     * @param ctx    [Context]
     * @param config 图片加载配置信息
     */
    fun clear(ctx: Context, config: T)
}