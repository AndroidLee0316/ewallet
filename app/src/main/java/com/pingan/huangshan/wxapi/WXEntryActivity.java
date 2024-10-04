package com.pingan.huangshan.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.swiftfintech.pay.MainApplication;
import com.swiftfintech.pay.activity.UnifiedPay;

/**
 * Created by zhuangjiguang on 2020/11/16.
 */
public class WXEntryActivity extends Activity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if(MainApplication.MINI_PROGRAM_TAG) {
      WXEntryActivity.this.finish();
      UnifiedPay.sendTag(this);
    }

  }
}