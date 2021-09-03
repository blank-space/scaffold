package com.bsnl.sample.pkg.feature.hook

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
class DrawableHook : XC_MethodHook() {

    override fun afterHookedMethod(param: MethodHookParam) {
        super.afterHookedMethod(param)
        val imageView = param.thisObject as ImageView
        imageView.drawable?.let {
            checkDrawable(imageView, it)
        }

    }

    private fun checkDrawable(imageView: ImageView, drawable: Drawable) {
        val width = imageView.width
        val height = imageView.height
        if (width > 0 && height > 0) {
            checkBitmapWH(drawable, width, height)
        } else {
            imageView.viewTreeObserver.addOnPreDrawListener(object :
                ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val w = imageView.width
                    val h = imageView.height
                    if (w > 0 && h > 0) {
                        checkBitmapWH(drawable, w, h)
                    }
                    imageView.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }

            })
        }
    }

    private fun checkBitmapWH(bitmap: Drawable, width: Int, height: Int) {
        if (bitmap.intrinsicWidth >= width * 2 && bitmap.intrinsicHeight >= height * 2) {
            warn(
                bitmap.intrinsicWidth,
                bitmap.intrinsicHeight,
                width,
                height)
       }
    }


    private fun warn(
        bitmapWidth: Int,
        bitmapHeight: Int,
        viewWidth: Int,
        viewHeight: Int
    ) {
        val sb = StringBuilder("======= Drawable too large:")
            .append(" real size :(")
            .append(bitmapWidth).append(",")
            .append(bitmapHeight).append("),")
            .append("but desired size :(")
            .append(viewWidth).append(",")
            .append(viewHeight).append(") =======")
        L.w(sb.toString())
    }

}