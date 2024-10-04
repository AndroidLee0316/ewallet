package com.pasc.lib.sm;


import android.util.Base64;

/**
 * @author yangzijian
 * @date 2019/3/11
 * @des
 * @modify
 **/
public class BASE64Decoder {
    public byte[] decodeBuffer(String data){
        return Base64.decode (data, Base64.DEFAULT);
    }

}
