package com.pasc.lib.netpay.resp;

/**
 * 平安付启动插件回调
 * Created by zhangcan603 on 2018/2/10.
 */

public class PafResp {

    /**
     * 1000 成功
     * -1001 关闭/取消
     * 2002 签名不正确
     * 2007 商户不存在或不允许授权
     * 2017 签名串必要参数缺失
     */
    private String resultCode;
    private String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
