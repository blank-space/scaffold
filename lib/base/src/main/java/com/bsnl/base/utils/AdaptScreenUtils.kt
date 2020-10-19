package com.bsnl.base.utils

import android.util.DisplayMetrics
import android.content.res.Resources;
import android.util.Log;
import com.bsnl.base.BaseApp

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author : LeeZhaoXing
 * @date   : 2020/4/29
 * @desc   : 屏幕适配工具
 */
class AdaptScreenUtils private constructor() {
    companion object {
        private var sMetricsFields: MutableList<Field>? = null

        /**
         * Adapt for the horizontal screen, and call it in [android.app.Activity.getResources].
         */
        fun adaptWidth(resources: Resources, designWidth: Int): Resources {
            val newXdpi: Float =
                resources.getDisplayMetrics().widthPixels * 72f / designWidth
            applyDisplayMetrics(resources, newXdpi)
            return resources
        }

        /**
         * Adapt for the vertical screen, and call it in [android.app.Activity.getResources].
         */
        fun adaptHeight(resources: Resources, designHeight: Int): Resources {
            return adaptHeight(resources, designHeight, false)
        }

        /**
         * Adapt for the vertical screen, and call it in [android.app.Activity.getResources].
         */
        fun adaptHeight(
            resources: Resources,
            designHeight: Int,
            includeNavBar: Boolean
        ): Resources {
            val screenHeight: Float = (resources.getDisplayMetrics().heightPixels
                    + if (includeNavBar) getNavBarHeight(resources) else 0) * 72f
            val newXdpi = screenHeight / designHeight
            applyDisplayMetrics(resources, newXdpi)
            return resources
        }

        private fun getNavBarHeight(resources: Resources): Int {
            val resourceId: Int =
                resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId != 0) {
                resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }

        /**
         * @param resources The resources.
         * @return the resource
         */
        fun closeAdapt(resources: Resources): Resources {
            val newXdpi: Float = Resources.getSystem().getDisplayMetrics().density * 72f
            applyDisplayMetrics(resources, newXdpi)
            return resources
        }

        /**
         * Value of pt to value of px.
         *
         * @param ptValue The value of pt.
         * @return value of px
         */
        fun pt2Px(ptValue: Float): Int {
            val metrics: DisplayMetrics = BaseApp.application.getResources().getDisplayMetrics()
            return (ptValue * metrics.xdpi / 72f + 0.5).toInt()
        }

        /**
         * Value of px to value of pt.
         *
         * @param pxValue The value of px.
         * @return value of pt
         */
        fun px2Pt(pxValue: Float): Int {
            val metrics: DisplayMetrics = BaseApp.application.getResources().getDisplayMetrics()
            return (pxValue * 72 / metrics.xdpi + 0.5).toInt()
        }

        private fun applyDisplayMetrics(resources: Resources, newXdpi: Float) {
            resources.getDisplayMetrics().xdpi = newXdpi
            BaseApp.application.getResources().getDisplayMetrics().xdpi = newXdpi
            applyOtherDisplayMetrics(resources, newXdpi)
        }

        val preLoadRunnable: Runnable
            get() = Runnable { preLoad() }

        private fun preLoad() {
            applyDisplayMetrics(
                Resources.getSystem(),
                Resources.getSystem().getDisplayMetrics().xdpi
            )
        }

        private fun applyOtherDisplayMetrics(
            resources: Resources,
            newXdpi: Float
        ) {
            if (sMetricsFields == null) {
                sMetricsFields = ArrayList()
                var resCls: Class<*>? = resources.javaClass
                var declaredFields: Array<Field> = resCls!!.declaredFields
                while (declaredFields != null && declaredFields.size > 0) {
                    for (field in declaredFields) {
                        if (field.getType().isAssignableFrom(DisplayMetrics::class.java)) {
                            field.setAccessible(true)
                            val tmpDm =
                                getMetricsFromField(resources, field)
                            if (tmpDm != null) {
                                sMetricsFields!!.add(field)
                                tmpDm.xdpi = newXdpi
                            }
                        }
                    }
                    resCls = resCls!!.superclass
                    declaredFields = resCls?.declaredFields ?: break
                }
            } else {
                applyMetricsFields(resources, newXdpi)
            }
        }

        private fun applyMetricsFields(resources: Resources, newXdpi: Float) {
            for (metricsField in sMetricsFields!!) {
                try {
                    val dm = metricsField.get(resources) as DisplayMetrics
                    if (dm != null) dm.xdpi = newXdpi
                } catch (e: Exception) {
                    Log.e("AdaptScreenUtils", "applyMetricsFields: $e")
                }
            }
        }

        private fun getMetricsFromField(resources: Resources, field: Field): DisplayMetrics? {
            return try {
                field.get(resources)
            } catch (ignore: Exception) {
                null
            } as DisplayMetrics?
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}