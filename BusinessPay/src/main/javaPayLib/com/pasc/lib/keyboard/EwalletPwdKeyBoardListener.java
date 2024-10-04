package com.pasc.lib.keyboard;

/**
 * @author yangzijian
 * @date 2019/3/10
 * @des
 * @modify
 **/
public interface EwalletPwdKeyBoardListener {
    /***
     *
     * @param password 密码
     * @param isInvalidatePwd 是否为简单密码
     */
    void onPasswordInputFinish(String password,boolean isInvalidatePwd);

    void addPassWord(int currentIndex, int totalLength);

    void removePassWord(int currentIndex, int totalLength);

    void clearPassWord(int currentIndex, int totalLength);
}
