package com.pasc.ewallet.dev.pay.unionpay.util;

import android.content.Context;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;

/**
 * Created by zhuangjiguang on 2021/3/1.
 */
public class UnionPayUtil {
  /**
   * 微信
   * @param appPayRequest
   */
  public static void payWX(Context context, String appPayRequest){
    UnifyPayRequest msg = new UnifyPayRequest();
    msg.payChannel = UnifyPayRequest.CHANNEL_WEIXIN;
    msg.payData = appPayRequest;
    UnifyPayPlugin.getInstance(context).sendPayRequest(msg);
  }

  /**
   * 支付宝小程序支付方式
   * @param appPayRequest
   */
  public static void payAliPayMiniPro(Context context, String appPayRequest){
    UnifyPayRequest msg = new UnifyPayRequest();
    msg.payChannel = UnifyPayRequest.CHANNEL_ALIPAY_MINI_PROGRAM;
    msg.payData = appPayRequest;
    UnifyPayPlugin.getInstance(context).sendPayRequest(msg);
  }
}
