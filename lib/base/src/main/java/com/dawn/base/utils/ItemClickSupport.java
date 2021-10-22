package com.dawn.base.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dawn.base.R;
import com.dawn.base.ui.page.base.BindingViewDelegate;
import com.drakeet.multitype.MultiTypeAdapter;

import java.util.HashSet;

/**
 * @author : LeeZhaoXing
 * @date : 2021/6/30
 * @desc :
 */
public class ItemClickSupport {
    private final RecyclerView mRecyclerView;

    private OnItemClick mOnItemClick;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClick != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                MultiTypeAdapter adapter = (MultiTypeAdapter) mRecyclerView.getAdapter();
                try {
                    mOnItemClick.onItemClick(adapter, v, holder.getLayoutPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private View.OnClickListener mOnChildClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClick != null) {
                BindingViewDelegate.BindingViewHolder holder = (BindingViewDelegate.BindingViewHolder) v.getTag(R.id.holder);
                MultiTypeAdapter adapter = (MultiTypeAdapter) mRecyclerView.getAdapter();
                try {
                    mOnItemClick.onItemChildClick(adapter, v, holder.getLayoutPosition() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemClick != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                MultiTypeAdapter adapter = (MultiTypeAdapter) mRecyclerView.getAdapter();
                try {
                    mOnItemClick.onItemLongClick(adapter, v, holder.getLayoutPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };

    private View.OnLongClickListener mOnChildLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemClick != null) {
                BindingViewDelegate.BindingViewHolder holder = (BindingViewDelegate.BindingViewHolder) v.getTag(R.id.holder);
                MultiTypeAdapter adapter = (MultiTypeAdapter) mRecyclerView.getAdapter();
                try {
                    mOnItemClick.onItemChildLongClick(adapter, v, holder.getLayoutPosition() );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            BindingViewDelegate.BindingViewHolder holder = (BindingViewDelegate.BindingViewHolder) mRecyclerView.getChildViewHolder(view);
            int itemViewType = holder != null ? holder.getItemViewType() : -1;
            if(itemViewType != -1 ) {
                HashSet<Integer> clickViewIds = holder.getChildClickViewIds();
                if(clickViewIds != null && clickViewIds.size() > 0) {
                    for (int id : clickViewIds) {
                        View v = holder.getView(id);
                        if(v != null) {
                            v.setTag(R.id.holder, holder);
                            v.setOnClickListener(mOnChildClickListener);
                            v.setOnLongClickListener(mOnChildLongClickListener);
                        }
                    }
                }
                view.setOnClickListener(mOnClickListener);
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            BindingViewDelegate.BindingViewHolder holder = (BindingViewDelegate.BindingViewHolder) mRecyclerView.getChildViewHolder(view);
            int itemViewType = holder != null ? holder.getItemViewType() : -1;
            if(itemViewType != -1 ) {
                HashSet<Integer> clickViewIds = holder.getChildClickViewIds();
                if(clickViewIds != null && clickViewIds.size() > 0) {
                    for (int id : clickViewIds) {
                        View v = holder.getView(id);
                        if(v != null) {
                            v.setTag(R.id.holder, holder);
                            v.setOnClickListener(null);
                            v.setOnLongClickListener(null);
                        }
                    }
                }
                view.setOnClickListener(null);
                view.setOnLongClickListener(null);
            }
        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_click_support, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickSupport addTo(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
        }
        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click_support, null);
    }

    public static abstract class OnItemClick<T extends MultiTypeAdapter> {
        public void onItemClick(T adapter, View view, int position) {
        }

        public void onItemLongClick(T adapter, View view, int position) {

        }

        public void onItemChildClick(T adapter, View view, int position) {

        }

        public void onItemChildLongClick(T adapter, View view, int position) {
        }
    }

}