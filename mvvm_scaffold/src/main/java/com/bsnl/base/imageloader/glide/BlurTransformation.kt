package com.bsnl.base.imageloader.glide

import android.graphics.Bitmap
import androidx.annotation.IntRange
import com.bsnl.base.utils.FastBlur
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   : 高斯模糊
 */
class BlurTransformation(@IntRange(from = 0) radius: Int) :
    BitmapTransformation() {
    private val mRadius: Int
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap?{
        return FastBlur.doBlur(toTransform, mRadius, true)
    }

    override fun equals(o: Any?): Boolean {
        return o is BlurTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        const val DEFAULT_RADIUS = 15
        private val ID = BlurTransformation::class.java.name
        private val ID_BYTES = ID.toByteArray(CHARSET)
    }

    init {
        mRadius = radius
    }
}



