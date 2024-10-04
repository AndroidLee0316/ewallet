package com.pasc.lib.netpay;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import com.pasc.business.ewallet.common.utils.Util;

import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;

import retrofit2.HttpException;

/**
 * Created by duyuan797 on 17/5/19.
 */

public class ExceptionHandler {

    public static final String CONNECT_EXCEPTION = "网络不佳，请稍后重试";
    public static final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请稍后重试";
    public static final String MALFORMED_JSON_EXCEPTION = "数据解析异常";
    public static final String SYSTEM_ERROR_EXCEPTION = "系统维护中，请稍后重试";

    private static final String SYSTEM_ERROR_EXCEPTION500MSG = "系统错误";


    public static String convertMsg(String code,String msg){
        if ("500".equals (code)  && SYSTEM_ERROR_EXCEPTION500MSG.equals (msg)){
            return SYSTEM_ERROR_EXCEPTION;
        }else if (Util.isEmpty (msg)){
            return CONNECT_EXCEPTION;
        }else if ("500".equals (code)){
            return SYSTEM_ERROR_EXCEPTION;
        }
        return msg;
    }


    public static boolean isSystemError(String errorCode){
        return "500".equalsIgnoreCase (errorCode) ||
                "500".equalsIgnoreCase (errorCode) || "501".equalsIgnoreCase (errorCode)
                || "502".equalsIgnoreCase (errorCode) || "503".equalsIgnoreCase (errorCode)
                || "504".equalsIgnoreCase (errorCode) || "505".equalsIgnoreCase (errorCode);
    }

    public static String handleException(Throwable throwable) {
        if (throwable == null) {
            return CONNECT_EXCEPTION;
        }
        throwable.printStackTrace();
        if (throwable instanceof NoSuchElementException || throwable instanceof CancellationException) {
            return CONNECT_EXCEPTION;
        } else if (throwable instanceof MalformedJsonException || throwable instanceof JsonSyntaxException) {
            return MALFORMED_JSON_EXCEPTION;
        } else if (throwable instanceof SocketTimeoutException) {
            return SOCKET_TIMEOUT_EXCEPTION;
        } else if (throwable instanceof ApiError) {
            return throwable.getMessage();
        }else if (throwable instanceof ApiV2Error) {
            return throwable.getMessage();
        } else {
            return CONNECT_EXCEPTION;
        }
    }

    public static int getExceptionWithCode(Throwable throwable) {
        if (throwable==null){
            return -1;
        }
        if (throwable instanceof ApiError) {
            ApiError apiError = (ApiError) throwable;
            return apiError.getCode();
        } else if (throwable instanceof HttpException){
            return ((HttpException) throwable).code ();
        }else {
            return -1;
        }
    }

    public static String getExceptionV2WithCode(Throwable throwable) {
        if (throwable==null){
            return "-1";
        }
        if (throwable instanceof ApiV2Error) {
            ApiV2Error apiError = (ApiV2Error) throwable;
            return apiError.getCode();
        } else if (throwable instanceof HttpException){
            return ((HttpException) throwable).code ()+"";
        } else {
            return "-1";
        }
    }
}
