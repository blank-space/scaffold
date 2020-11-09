package com.bsnl.common.utils

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bsnl.base.log.L
import com.bsnl.common.iface.OnItemClickListener


/**
 * @author : LeeZhaoXing
 * @date : 2020/6/17
 * @desc :
 */
object RecyclerViewUtil {

    fun initRecyclerView(recyclerView: RecyclerView?, adapter: RecyclerView.Adapter<*>?) {

        requireNotNull(recyclerView, { "recyclerView cannot null" })
        requireNotNull(adapter, { "adapter cannot null" })

        recyclerView.also {
            (it.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
            if (it.layoutManager == null) {
                it.layoutManager = LinearLayoutManager(recyclerView.context)
            }
            it.setHasFixedSize(true)
            it.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }


    fun setOnItemClickListener(
        recyclerView: RecyclerView?,
        listener: OnItemClickListener,
        vararg ids: Int?
    ) {
        recyclerView?.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            val gestureDetector =
                GestureDetector(recyclerView.context, object : GestureDetector.OnGestureListener {
                    override fun onShowPress(e: MotionEvent?) {
                    }

                    override fun onSingleTapUp(e: MotionEvent?): Boolean {
                        e?.let {
                            recyclerView.findChildViewUnder(it.x, it.y)?.let { child ->
                                val clickView = findClickView(child, e.rawX, e.rawY, *ids)
                                var position = recyclerView.getChildLayoutPosition(child)
                                if (position >= 0) {
                                    if (clickView == child) {
                                        listener.onItemClick(clickView, position)
                                    } else {
                                        listener.onChildClick(clickView, position)
                                    }
                                    return true
                                }

                            }
                        }
                        return false
                    }

                    override fun onDown(e: MotionEvent?): Boolean {
                        return false
                    }

                    override fun onFling(
                        e1: MotionEvent?,
                        e2: MotionEvent?,
                        velocityX: Float,
                        velocityY: Float
                    ): Boolean {
                        return false
                    }

                    override fun onScroll(
                        e1: MotionEvent?,
                        e2: MotionEvent?,
                        distanceX: Float,
                        distanceY: Float
                    ): Boolean {
                        return false
                    }

                    override fun onLongPress(e: MotionEvent?) {
                    }
                })

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    /**
     * 查找需要响应点击事件的View
     *
     * @param view itemView
     * @param x    屏幕上x坐标
     * @param y    屏幕上y坐标
     * @param ids  需要响应点击事件的控件id
     */
    private fun findClickView(view: View, x: Float, y: Float, vararg ids: Int?): View {
        if (view !is ViewGroup || ids == null || ids.size == 0) {
            return view
        }
        var clickView = view
        val location = IntArray(2)
        for (id in ids) {
            //L.d("id is :${id}")
            val child = view.findViewById<View>(id!!) ?: continue
            child.getLocationOnScreen(location)
            if (x >= location[0] && x <= location[0] + child.width && y >= location[1] && y <= location[1] + child.height) {
                clickView = child
            }
        }
        return clickView
    }
}