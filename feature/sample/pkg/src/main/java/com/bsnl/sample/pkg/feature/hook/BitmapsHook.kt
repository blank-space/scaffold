package com.bsnl.sample.pkg.feature.hook

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bsnl.base.log.L
import de.robv.android.xposed.XC_MethodHook

/**
 * @author : LeeZhaoXing
 * @date   : 2021/2/22
 * @desc   :
 */
class BitmapsHook : XC_MethodHook() {

    override fun afterHookedMethod(param: MethodHookParam) {
        super.afterHookedMethod(param)
        val imageView = param.thisObject as ImageView
        imageView.drawable?.let {
            checkBitMap(imageView, it)
        }

    }

    private fun checkBitMap(imageView: ImageView, drawable: Drawable) {
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            bitmap?.let {
                val width = imageView.width
                val height = imageView.height
                if (width > 0 && height > 0) {
                    checkBitmapWH(bitmap, width, height)
                } else {
                    imageView.viewTreeObserver.addOnPreDrawListener(object :
                        ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val w = imageView.width
                            val h = imageView.height
                            if (w > 0 && h > 0) {
                                checkBitmapWH(bitmap, w, h)
                            }
                            imageView.viewTreeObserver.removeOnPreDrawListener(this)
                            return true
                        }

                    })
                }
            }
        }
    }

    private fun checkBitmapWH(bitmap: Bitmap, width: Int, height: Int) {
        if (bitmap.width >= width * 2 && bitmap.height >= height * 2) {
            warn(
                bitmap.width,
                bitmap.height,
                width,
                height
            )
        }
    }


    private fun warn(
        bitmapWidth: Int,
        bitmapHeight: Int,
        viewWidth: Int,
        viewHeight: Int,
    ) {
        val sb = StringBuilder("======= Bitmap too large:")
            .append(" real size :(")
            .append(bitmapWidth).append(",")
            .append(bitmapHeight).append("),")
            .append("but desired size :(")
            .append(viewWidth).append(",")
            .append(viewHeight).append(") =======")
        L.w(sb.toString())
    }

}