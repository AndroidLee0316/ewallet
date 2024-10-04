package com.pasc.lib.netpay;

/**
 * Created by yangwen881 on 17/2/24.
 */

public class ApiError extends RuntimeException {

    private int code;

    private String msg;

    public ApiError(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override public String toString() {
        return "ApiError{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
