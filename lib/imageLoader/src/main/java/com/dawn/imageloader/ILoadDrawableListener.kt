package com.dawn.imageloader

import android.graphics.drawable.Drawable

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
interface ILoadDrawableListener {

    fun onGetDrawable(drawable: Drawable)
}