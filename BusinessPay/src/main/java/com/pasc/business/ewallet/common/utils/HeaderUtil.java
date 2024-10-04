package com.pasc.business.ewallet.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.pasc.business.ewallet.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author yangzijian
 * @date 2018/7/16
 * @des 公共头信息
 * @modify
 **/
public class HeaderUtil {
    public static Map<String, String> getHeaders(Context context,boolean isDev) {
//        Context context = AppProxy.getInstance ().getContext ();
        Map<String, String> commonHeaders = new HashMap<>();
//        /*****设备号****/
//        commonHeaders.put ("x-device-id", DeviceUtils.getDeviceId (context));
//        /******设备的厂商名****/
        commonHeaders.put ("x-app-platform", Build.BRAND);
//        /****设备系统类型 2：android、1：ios、1|2：h5-pc h5-ios ****/
        commonHeaders.put ("x-os-type", "2");
//        /****屏幕分辨率  屏幕宽x屏幕高*****/
//        commonHeaders.put ("x-screen-dpi", ScreenUtils.getScreenWidth () + "x" + ScreenUtils.getScreenHeight ());
//        /****ROM版本号****/
        commonHeaders.put ("x-os-name", Build.MODEL);
//        /*****操作系统版本****/
        commonHeaders.put ("x-os-version", "Android" + Build.VERSION.RELEASE);
//        /*****app version name *****/
//        commonHeaders.put ("x-app-version", AppUtils.getVersionName ());
        commonHeaders.put ("x-app-version", BuildConfig.X_SDK_VERSION);
        commonHeaders.put ("x-app-package-name", context.getPackageName ());
//        /*****app version name *****/
        commonHeaders.put ("x-sdk-version", BuildConfig.X_SDK_VERSION);
//        commonHeaders.put ("x-auth-channel", Constants.IN_CHANNEL_ID);
//
//        // 内部测试需添加isTest标签
//        if (isDev){
//            commonHeaders.put ("isTest", "true");
//        }
//
//        /*****app 名称*****/
//        try {
//            commonHeaders.put ("x-app-name", URLEncoder.encode (getAppName (context), "UTF-8"));
//        }catch (Exception e){}
//
//        /**** 渠道号***/
//        //commonHeaders.put ("x-channel", getAppMetaData (AppProxy.getInstance ().getContext (), "SMT_CHANNEL", "product"));
//        /***后台版本***/
//        commonHeaders.put ("x-api-version", "1.4.0");
        commonHeaders.put ("Content-Type", "application/json");


        return commonHeaders;

    }

    private static String ipAddress="";
    public static Map<String, String> dynamicHeaders() {
//        Context context = AppProxy.getInstance ().getContext ();
        Map<String, String> dynamicHeaders = new HashMap<>();
//        dynamicHeaders.put ("token", UserManager.getInstance().getToken ());
//        //其他两个动态header x-api-sign和x-api-timestamp 放在拦截器 HttpRequestInterceptor
//        if (TextUtils.isEmpty (ipAddress)){
//            ipAddress = Util.getIPAddress(context);
//        }
//        dynamicHeaders.put ("x-device-ip",ipAddress);
//        Location payLocation= UserManager.getInstance().getPayLocation ();
//        if (payLocation!=null){
////            dynamicHeaders.put ("x-device-location",payLocation.getLongitude ()+","+payLocation.getLatitude ());
//            //2019/6/25 银行改的
//            dynamicHeaders.put ("x-device-location",payLocation.getLongitude ()+"/"+payLocation.getLatitude ());
//
//        }

        return dynamicHeaders;
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager ();
            PackageInfo packageInfo = packageManager.getPackageInfo (
                    context.getPackageName (), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources ().getString (labelRes);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return "";
    }

}
