package com.dawn.base.widget

import android.view.View

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/19
 * @desc   : 防止点击抖动
 */
abstract class ClickProtector : View.OnClickListener {
    // 点击时间间隔: ms
    private var delay: Long = 1000

    // 实际的点击事件
    abstract fun onRealClick(v: View?)

    /**
     * 设置点击间隔
     *
     * @param delay 单位:ms
     */
    fun delay(delay: Long): ClickProtector {
        this.delay = delay
        return this
    }

    override fun onClick(v: View) {
        val key: Int = v.hashCode()
        val lastTime = v.getTag(key) as? Long ?: 0
        // 没点击过 或者 本次点击跟上次点击时间差小于delay，就拦截
        if (System.currentTimeMillis() - lastTime < delay) {
            return
        }
        // 触发点击事件
        onRealClick(v)
        // 记录本次点击时间
        v.setTag(key, System.currentTimeMillis())
    }
}