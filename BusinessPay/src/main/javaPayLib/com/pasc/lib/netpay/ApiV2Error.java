package com.pasc.lib.netpay;

/**
 * Created by yangwen881 on 17/2/24.
 */

public class ApiV2Error extends RuntimeException {

    private String code;

    private String msg;

    public ApiV2Error(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override public String toString() {
        return "ApiV2Error{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
