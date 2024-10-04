package com.pasc.lib.sm;


import android.util.Base64;

/**
 * @author yangzijian
 * @date 2019/3/11
 * @des
 * @modify
 **/
public class BASE64Encoder {
    public String encode(byte[] encrypted){
        return String.valueOf (Base64.encode (encrypted, Base64.DEFAULT));
    }

}
