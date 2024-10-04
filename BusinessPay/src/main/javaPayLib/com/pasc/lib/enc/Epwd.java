package com.pasc.lib.enc;

import com.pasc.business.ewallet.NotProguard;

/**
 * @date 2019-12-25
 * @des
 * @modify
 **/
@NotProguard
public class Epwd {
    {
        System.loadLibrary ("ewalletPascPay");
    }

    public  void addPassWord(String pwd,int mode){
        nativeAddPassWord (pwd,mode);
    }

    public  void removePassWord(){
        nativeRemovePassWord ();
    }

    public  void clearPassWord(){
        nativeClearPassWord ();
    }

    public  int currentLen(){
       return currentLength ();
    }

    public String getPwd(int mode){
        return nativeGetPassword (mode);
    }

    private native void nativeAddPassWord(String pwd,int mode);

    private native void nativeRemovePassWord();

    private native void nativeClearPassWord();

    private native int currentLength();

    private native String nativeGetPassword(int mode);
}
