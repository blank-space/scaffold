package com.dawn.base;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author : LeeZhaoXing
 * @date : 2021/9/2
 * @desc :
 */
public class DataResult<T> implements Serializable {
    private static final long serialVersionUID = 2690553609250007325L;
    public String code;
    public String msg;
    public long date;
    private T data;

    public boolean isError() {
        return code == null || !code.contentEquals("000000");
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
                "code=" + code +
                ", msg='" + msg + '\'' +
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