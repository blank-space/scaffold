package com.dawn.domain.usecase;

import android.os.Handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : LeeZhaoXing
 * @date : 2021/10/26
 * @desc :
 */
public class UseCaseThreadPoolScheduler implements UseCaseScheduler {
    public static final int POOL_SIZE = 2;
    public static final int MAX_POOL_SIZE = 4 * 2;
    public static final int FIXED_POOL_SIZE = 4;
    public static final int TIMEOUT = 30;
    final ThreadPoolExecutor mThreadPoolExecutor;
    private final Handler mHandler = new Handler();

    /**
     * 固定线程数的无界线程池
     */
    public UseCaseThreadPoolScheduler() {
        mThreadPoolExecutor = new ThreadPoolExecutor(FIXED_POOL_SIZE, FIXED_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    @Override
    public <V extends UseCase.ResponseValue> void notifyResponse(final V response,
                                                                 final UseCase.UseCaseCallback<V> useCaseCallback) {
        mHandler.post(() -> {
            if (null != useCaseCallback) {
                useCaseCallback.onSuccess(response);
            }
        });
    }

    @Override
    public <V extends UseCase.ResponseValue> void onError(
            final UseCase.UseCaseCallback<V> useCaseCallback) {
        mHandler.post(useCaseCallback::onError);
    }

}