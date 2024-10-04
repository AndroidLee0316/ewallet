package com.pasc.ewallet.dev.utils;

import com.pasc.business.ewallet.config.Constants;

/**
 * @date 2019/8/19
 * @des
 * @modify
 **/
public class AppConstant {
    public static final String key_pre_env = "key_pre_env";

    public static boolean isRelease() {
        return Constants.currentEnv == Constants.PAY_RELEASE_ENV;
    }

    public static boolean isSTG2() {
        return Constants.currentEnv == Constants.PAY_STG2_ENV;
    }

    public static String getTip() {
        if (isRelease ()) {
            return "正式环境";
        } else if (isSTG2 ()) {
            return "stg2-gateway，stg2环境";

        } else {
            return "purse-gateway，测试环境";
        }
    }

    public static String getMemberNo() {
        if (isRelease ()) {
            return Re_memberNo;
        } else if (isSTG2 ()) {
            return STG2_memberNo;

        } else {
            return TEST_memberNo;
        }
    }

    public static String getMerchantNo() {
        if (isRelease ()) {
            return Re_merchantNo;
        } else if (isSTG2 ()) {
            return STG2_merchantNo;

        } else {
            return TEST_merchantNo;
        }
    }

    public static String getSceneId() {
        if (isRelease ()) {
            return Re_sceneId;
        } else if (isSTG2 ()) {
            return STG2_sceneId;

        } else {
            return TEST_sceneId;
        }
    }

    public static String getIdNo() {
        if (isRelease ()) {
            return Re_idNo;
        } else if (isSTG2 ()) {
            return STG2_idNo;

        } else {
            return TEST_idNo;
        }
    }

    public static String getName() {
        if (isRelease ()) {
            return Re_name;
        } else if (isSTG2 ()) {
            return STG2_name;

        } else {
            return TEST_name;
        }
    }

    public static String getPhone() {
        if (isRelease ()) {
            return Re_phone;
        } else if (isSTG2 ()) {
            return STG2_phone;

        } else {
            return TEST_phone;
        }
    }

    public static String getCard() {
        if (isRelease ()) {
            return Re_Card;
        } else if (isSTG2 ()) {
            return STG2_Card;

        } else {
            return TEST_Card;
        }
    }
    // 7001900092618
    //public static String Re_memberNo = "5001900092518";
    //public static String Re_merchantNo = "80519000918";
    //public static String Re_memberNo = "5972011125724";
    //public static String Re_merchantNo = "18520000618";
    //public static String Re_memberNo = "6012100102012"; //东莞
    //public static String Re_merchantNo = "20121001012"; //东莞
    //public static String Re_sceneId = "000002"; //东莞
    public static String Re_memberNo = ""; //园区
    public static String Re_merchantNo = "71621000521"; //园区
    public static String Re_sceneId = "000124"; //园区
    public static String Re_idNo = "";
    public static String Re_name = "";
    public static String Re_phone = "";
    public static String Re_Card = "";

    //会员号
    //public static String STG2_memberNo = "7062043118211";//无锡、盐城
    //public static String STG2_memberNo = "8892009010713";//黄山
    //public static String STG2_memberNo = "8892009010713";//绵阳
    //public static String STG2_memberNo = "4502110032911"; //威海
    //public static String STG2_memberNo = "1000000000001"; //南通
    //public static String STG2_memberNo = "3002100090108"; //中山
    //public static String STG2_memberNo = "8892009010713"; //东莞
    //public static String STG2_memberNo = "4242147049201"; //园区
    //商户号
    //public static String STG2_merchantNo = "51420011111";//无锡
    //public static String STG2_merchantNo = "20220010103";//黄山
    //public static String STG2_merchantNo = "77720011222";//绵阳
    //public static String STG2_merchantNo = "28921020301"; //威海
    //public static String STG2_merchantNo = "90321000315"; //南通
    //public static String STG2_merchantNo = "30921000913"; //中山
    //public static String STG2_merchantNo = "77720011222"; //东莞
    //public static String STG2_merchantNo = "23621040323"; //园区
    //场景号
    //public static String STG2_sceneId = "";//无锡、盐城
    //public static String STG2_sceneId = "000124";//黄山
    //public static String STG2_sceneId = "000059";//绵阳
    //public static String STG2_sceneId = "000068"; //威海
    //public static String STG2_sceneId = "000001"; //南通
    //public static String STG2_sceneId = "000001"; //中山
    //public static String STG2_sceneId = "000003"; //东莞
    //public static String STG2_sceneId = "000124"; //园区

    public static String STG2_memberNo = ""; //南通
    public static String STG2_merchantNo = "82022020121"; //南通
    public static String STG2_sceneId = "000142"; //南通

    public static String STG2_idNo = "429006198809037926";
    public static String STG2_name = "周首一";
    public static String STG2_phone = "18880000000";
    public static String STG2_Card = "";

    public static String TEST_memberNo = "9191900082619";
    public static String TEST_merchantNo = "10819010716";
    public static String TEST_sceneId = "";
    public static String TEST_idNo = "140106196912222207";
    public static String TEST_name = "韦馨";
    public static String TEST_phone = "13005049912";
    public static String TEST_Card = "6230580000000052220";

}
