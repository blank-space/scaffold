package com.bsnl.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.adapter
 * email：893855882@qq.com
 * data：16/12/31
 */

public class GridSpaceItemIncludeEdgeDecoration extends RecyclerView.ItemDecoration {

    /**
     * 每行个数
     */
    private int mSpanCount;
    /**
     * 间距
     */
    private int mSpacing;
    /**
     * 距屏幕周围是否也有间距
     */
    private boolean mIncludeEdge;

    /**
     * 头部 不显示间距的item个数
     */
    private int mStartFromSize;
    /**
     * 尾部 不显示间距的item个数
     */
    private int mEndFromSize = 0;

    public GridSpaceItemIncludeEdgeDecoration(int spanCount, int spacing) {
        this(spanCount, spacing, true);
    }

    /**
     * @param spanCount   item 每行个数
     * @param spacing     item 间距
     * @param includeEdge item 距屏幕周围是否也有间距
     */
    public GridSpaceItemIncludeEdgeDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int lastPosition = state.getItemCount() - 1;
        int position = parent.getChildAdapterPosition(view);
        if (mStartFromSize <= position && position <= lastPosition - mEndFromSize) {

            // 减掉不设置间距的position
            position = position - mStartFromSize;
            int column = position % mSpanCount;

            // 瀑布流获取列方式不一样
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                column = ((StaggeredGridLayoutManager.LayoutParams) layoutParams).getSpanIndex();
            }

            if (mIncludeEdge) {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * ---------10--------
                 * 10   3+7   6+4    10
                 * ---------10--------
                 * 10   3+7   6+4    10
                 * ---------10--------
                 */
                outRect.left = mSpacing - column * mSpacing / mSpanCount;
                outRect.right = (column + 1) * mSpacing / mSpanCount;

                if (position < mSpanCount) {
                    outRect.top = mSpacing;
                }
                outRect.bottom = mSpacing;

            } else {
                /*
                 *示例：
                 * spacing = 10 ；spanCount = 3
                 * --------0--------
                 * 0   3+7   6+4    0
                 * -------10--------
                 * 0   3+7   6+4    0
                 * --------0--------
                 */
                outRect.left = column * mSpacing / mSpanCount;
                outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
                if (position >= mSpanCount) {
                    outRect.top = mSpacing;
                }
            }
        }
    }

    /**
     * 设置从哪个位置 开始设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     */
    public GridSpaceItemIncludeEdgeDecoration setStartFrom(int startFromSize) {
        this.mStartFromSize = startFromSize;
        return this;
    }

    /**
     * 设置从哪个位置 结束设置间距。
     *
     * @param endFromSize 一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    public GridSpaceItemIncludeEdgeDecoration setEndFromSize(int endFromSize) {
        this.mEndFromSize = endFromSize;
        return this;
    }

    /**
     * 设置从哪个位置 结束设置间距
     *
     * @param startFromSize 一般为HeaderView的个数 + 刷新布局(不一定设置)
     * @param endFromSize   默认为1，一般为FooterView的个数 + 加载更多布局(不一定设置)
     */
    public GridSpaceItemIncludeEdgeDecoration setNoShowSpace(int startFromSize, int endFromSize) {
        this.mStartFromSize = startFromSize;
        this.mEndFromSize = endFromSize;
        return this;
    }
}
