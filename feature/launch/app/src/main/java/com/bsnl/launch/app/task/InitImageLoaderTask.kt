package com.bsnl.launch.app.task

import com.bsnl.base.BaseApp
import com.bsnl.base.imageloader.BaseImageLoaderStrategy
import com.bsnl.base.imageloader.ImageLoader
import com.bsnl.base.imageloader.glide.GlideImageLoaderStrategy
import com.bsnl.base.log.L
import com.bsnl.faster.Task
import com.tencent.mmkv.MMKV

/**
 * @author : LeeZhaoXing
 * @date   : 2020/11/9
 * @desc   :
 */
class InitImageLoaderTask : Task(){
    override fun run() {
        //配置Glide加载策略
        ImageLoader.loadImgStrategy = GlideImageLoaderStrategy() as BaseImageLoaderStrategy<Any>
    }
}