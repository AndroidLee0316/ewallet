package com.pasc.business.ewallet.callback;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.pasc.business.ewallet.NotProguard;

/**
 * Created by zhuangjiguang on 2020/12/2.
 */
@NotProguard
public interface OnActivityLifecycleCallback {

  void onActivityResult(Activity activity, String payType, int requestCode, int resultCode, Intent data);
  void onNewIntent(Activity activity, String payType, Intent intent);
  void onRequestPermissionsResult(Activity activity, String payType, int requestCode,
      @NonNull String[] permissions, @NonNull int[] grantResults);
  void onDestroy(Activity activity, String payType);
}