package com.dawn.sample.pkg.feature.data.entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author : LeeZhaoXing
 * @date : 2021/11/7
 * @desc :
 */
public class WanResult<T> implements Serializable {
    public int errorCode;
    public String errorMsg;
    public long date;
    private T data;

    public boolean isError() {
        return errorCode != 0;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public @NotNull String toString() {
        return "{" +
                "code=" + errorCode +
                ", msg='" + errorMsg + '\'' +
                ", date=" + date +
                ", data=" + data +
                '}';
    }

    /**
     * 正常返回
     *
     * @return
     */
    public boolean isSuccessFul() {
        return !isError();
    }
}