package com.pasc.business.ewallet.business.util;

import android.text.TextUtils;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/2/28
 */
@NotProguard
public class AccountUtil {

    //可输入的最大金额
    public static final int INPUT_MONEY_NUM_BITS_MAX = 7;
    //可输入的小数点最大位数
    public static final int INPUT_MONEY_NUM_DOTS_AFTER_MAX = 2;
    //可输入最大的位数：小数点前7位+小数点+小数点后两位
    public static final int INPUR_MONEY_NUM_LENGTH_MAX = 10;


    /**
     * 格式化输入的字符串，防止转化为double崩溃
     * @param inputStr
     * @return
     */
    public static double formatInputMoneyNum(String inputStr){
        inputStr = inputStr.trim();
        if(TextUtils.isEmpty(inputStr)){
            return 0;
        }
        if (inputStr.contains(".") && inputStr.lastIndexOf(".") == inputStr.length()-1){//防止出现xx.这种会导致double强制转化崩溃的问题
            inputStr = inputStr.substring(0,inputStr.length()-1);
        }
        if(TextUtils.isEmpty(inputStr)){
            return 0;
        }
        double inputNum = Double.parseDouble(inputStr);
        return inputNum;
    }


    /**
     * 检测输入内容是否只有一个点
     * @param inputStr
     * @return
     */
    public static boolean checkAndAddPreInputMoneyOnlyDot(CharSequence inputStr){
        return ".".equals (inputStr.toString ().trim ());
    }

    /**
     * 检测输入的位数是否超过最大的位数,且小数点后是否超过两位数
     * @param inputStr
     * @return
     */
    public static boolean isInputMoneyExceedMax(String inputStr){
        if (inputStr.contains(".")){
            return inputStr.indexOf (".") > INPUT_MONEY_NUM_BITS_MAX;
        }else {
            return inputStr.length () > INPUT_MONEY_NUM_BITS_MAX;
        }
    }



    /**
     * 输入位数小数点后面
     * @param inputStr
     * @return
     */
    public static boolean isInputMoneyExceedBits(String inputStr){
        if (inputStr.contains(".")){
            return inputStr.length () - inputStr.indexOf (".") - 1 > INPUT_MONEY_NUM_DOTS_AFTER_MAX;
        }
        return false;
    }

    private static Map<String,String> cache=new HashMap<> ();

    private static final String key_phone="phone";
    public static void cache(String key,String value){
        if (!Util.isEmpty (key)){
            cache.put (key,value);
        }
    }
    public static void cachePhone(String memberNo,String phone){
        if (!Util.isEmpty (memberNo)) {
            cache (key_phone+memberNo,phone);
        }
    }
    public static boolean hasCachePhone(String memberNo) {
        String pp=UserManager.getInstance ().getPhoneNum ();
        if (!Util.isEmpty (memberNo) && !Util.isEmpty (pp)) {
            String phone=  cache.get (key_phone+memberNo);
            if (pp.equalsIgnoreCase (phone)){
                return true;
            }
        }
        return false;
    }
}
