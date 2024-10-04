package com.pasc.ewallet.dev.pay.cmbpay;

/**
 * Created by zhuangjiguang on 2020/11/10.
 */
public class CmbPayUtil {
  public static final String H5_URL_DEBUG = "http://121.15.180.66:801/netpayment/BaseHttp.dll?H5PayJsonSDK";
  public static final String H5_URL_RELEASE = "https://netpay.cmbchina.com/netpayment/BaseHttp.dll?H5PayJsonSDK";
  public static final String CMB_JUMP_URL = "cmbmobilebank://CMBLS/FunctionJump?action=gofuncid&funcid=200013&serverid=CMBEUserPay&requesttype=post&cmb_app_trans_parms_start=here";
}
