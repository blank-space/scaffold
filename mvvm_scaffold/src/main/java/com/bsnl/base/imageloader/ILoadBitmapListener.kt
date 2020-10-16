package com.bsnl.base.imageloader

import android.graphics.Bitmap

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
interface ILoadBitmapListener {

    fun onGetBitmap(bitmap: Bitmap)
}