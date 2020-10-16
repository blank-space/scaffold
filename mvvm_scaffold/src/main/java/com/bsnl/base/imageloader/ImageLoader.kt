/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bsnl.base.imageloader

import android.content.Context

/**
 * [ImageLoader] 使用策略模式和建造者模式,可以动态切换图片请求框架(比如说切换成 Picasso )
 * 当需要切换图片请求框架或图片请求框架升级后变更了 Api 时
 * 这里可以将影响范围降到最低,所以封装 [ImageLoader] 是为了屏蔽这个风险
 *
 */
object ImageLoader {
    /**
     * 可在运行时随意切换 [BaseImageLoaderStrategy]
     *
     * @param strategy
     */
    var loadImgStrategy: BaseImageLoaderStrategy<Any>? = null

    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
    </T> */
    fun <T : ImageConfig> loadImage(context: Context?, config: T) {
        loadImgStrategy!!.loadImage(context!!, config)
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context
     * @param config
     * @param <T>
    </T> */
    fun <T : ImageConfig> clear(context: Context?, config: T) {
        loadImgStrategy!!.clear(context!!, config)
    }
}