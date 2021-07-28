package com.bsnl.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class BaseHttpResult<T> implements Serializable {
    private static final long serialVersionUID = 2690553609250007325L;
    public String code;
    public String msg;
    public long date;
    private T data;

    public boolean isError() {
        return !code.contentEquals("0");
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
