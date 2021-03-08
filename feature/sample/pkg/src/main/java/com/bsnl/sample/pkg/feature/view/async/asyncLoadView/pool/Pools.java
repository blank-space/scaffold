package com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bsnl.sample.pkg.feature.view.async.asyncLoadView.pool.iface.Pool;

import java.util.HashMap;


public final class Pools {

    private Pools() {
        /* do nothing - hiding constructor */
    }

    /**
     * Simple (non-synchronized) pool of objects.
     *
     * @param <T> The pooled type.
     */
    public static class SimplePool<T> implements Pool<T> {
        public final HashMap<Integer, T> mPool;

        private int mPoolSize;
        private int mMaxSize;

        /**
         * Creates a new instance.
         *
         * @param maxPoolSize The max pool size.
         * @throws IllegalArgumentException If the max pool size is less than zero.
         */
        public SimplePool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            mMaxSize = maxPoolSize;
            mPool = new HashMap<>();
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(Integer layoutId) {
            if (layoutId == null || layoutId <= 0) {
                return null;
            }
            if (mPoolSize > 0) {
                T instance = (T) mPool.get(layoutId);
                mPoolSize--;
                return instance;
            }
            return null;
        }

        @Override
        public boolean put(Integer layoutId, @NonNull T instance) {
            if (layoutId == null || layoutId <= 0) {
                return false;
            }
            if (isInPool(layoutId)) {
                throw new IllegalStateException("Already in the pool!");
            }
            if (mPoolSize < mMaxSize) {
                mPool.put(layoutId, instance);
                mPoolSize++;
                return true;
            }
            return false;
        }

        private boolean isInPool(int layoutId) {
            for (int key : mPool.keySet()) {
                if (key == layoutId) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public T recycle(Integer layoutId) {
            if (layoutId == null || layoutId <= 0) {
                return null;
            }
            if (mPoolSize > 0) {
                T t = (T) mPool.remove(layoutId);
                mPoolSize--;
                return t;
            }
            return null;
        }
    }

}