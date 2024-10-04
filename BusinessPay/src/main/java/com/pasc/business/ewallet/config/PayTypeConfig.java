package com.pasc.business.ewallet.config;

import com.pasc.business.ewallet.NotProguard;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuangjiguang on 2020/12/1.
 */
@NotProguard
public class PayTypeConfig {

  private static volatile PayTypeConfig mInstance = null;
  private PayTypeConfig() {

  }

  public static PayTypeConfig getInstance() {
    if (mInstance == null) {
      synchronized (PayTypeConfig.class) {
        if (mInstance == null) {
          mInstance = new PayTypeConfig();
        }
      }
    }
    return mInstance;
  }

  //序列化破坏单例模式
  //private Object readResolve(){
  //  return mInstance;
  //}

  public Map<String, PayBehaviorHandler> mCustomerBehaviors = new HashMap<>(16);

  public PayTypeConfig addCustomPayType(String payType, PayBehaviorHandler payBehaviorHandler) {
    mCustomerBehaviors.put(payType, payBehaviorHandler);
    return this;
  }

  public Map<String, PayBehaviorHandler> getCustomerBehaviors() {
    return mCustomerBehaviors;
  }
}
