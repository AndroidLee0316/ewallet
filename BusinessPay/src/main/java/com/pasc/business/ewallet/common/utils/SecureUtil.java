package com.pasc.business.ewallet.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import org.bouncycastle.util.encoders.Hex;

/**
 * 加解密相关工具类
 * Created by ex-huangzhiyi001 on 2019/3/21.
 */
public class SecureUtil {
    public static String getSHA1 (Context context) {
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++){
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1){
                    hexString.append("0");
                }
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param content
     * @param salt    16位的随机数一个
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSaltMD5 (String content, String salt)
            throws NoSuchAlgorithmException {
        if (TextUtils.isEmpty(salt)){
            // 生成一个16位的随机数
            SecureRandom random = new SecureRandom();
            StringBuilder sBuilder = new StringBuilder(16);
            sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
            int len = sBuilder.length();
            if (len < 16){
                for (int i = 0; i < 16 - len; i++){
                    sBuilder.append("0");
                }
            }
            // 生成最终的加密盐
            salt = sBuilder.toString();
        } else{
            if (salt.length() < 16){
                //不足16位，添加
                StringBuilder sb = new StringBuilder(16);
                sb.append(salt);
                for (int i = 0; i < 16 - salt.length(); i++){
                    sb.append("0");
                }
                salt = sb.toString();
            }else {
                salt = salt.substring(0, 16);
            }
        }
        content = md5Hex(content + salt);
        char[] cs = new char[48];
        //混合
        char c;
        //前24位处理
        for (int i = 0; i < 24; i += 3){
            c = salt.charAt(i / 3);
            cs[i] = content.charAt(i / 3 * 2);
            cs[i + 1] = c;
            cs[i + 2] = content.charAt(i / 3 * 2 + 1);
        }
        //后24位处理
        for (int i = 24; i < 48; i += 3){
            c = salt.charAt(i / 3);
            cs[i] = c;
            cs[i + 1] = content.charAt(i / 3 * 2);
            cs[i + 2] = content.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }

    /**
     * @param content
     * @param md5str
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean getSaltverifyMD5 (String content, String md5str)
            throws NoSuchAlgorithmException {
        char[] cs1 = new char[32];//原始内容+盐后的md5值
        char[] cs2 = new char[16];//盐
        //提取
        //前24位处理
        for (int i = 0; i < 24; i += 3){
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        //后24位处理
        for (int i = 24; i < 48; i += 3){
            cs1[i / 3 * 2] = md5str.charAt(i + 1);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i);
        }

        String salt = new String(cs2);
        return md5Hex(content + salt).equals(String.valueOf(cs1));
    }

    /**
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String md5Hex (String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(str.getBytes());
        return new String(Hex.encode(digest));
    }


//    public static void main(String[] args){
//        String s = "123asd34gt";
//        try{
//            String saltMD5 = getSaltMD5(s);
//            System.out.println("saltMD5 -> "+saltMD5);
//            boolean saltverifyMD5 = getSaltverifyMD5(s, saltMD5);
//            System.out.println("saltverifyMD5 -> "+saltverifyMD5);
//        } catch (NoSuchAlgorithmException e){
//            e.printStackTrace();
//        }
//    }

}
