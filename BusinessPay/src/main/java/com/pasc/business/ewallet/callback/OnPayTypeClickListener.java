package com.pasc.business.ewallet.callback;

import android.app.Activity;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;

/**
 * 收银台支付类型点击回调Listener
 * <p>
 * Created by zhuangjiguang on 2020/12/1.
 */
@NotProguard
public abstract class OnPayTypeClickListener {

    public abstract void onPayTypeChoose(Activity activity, String payType, PayResp payBean);

}