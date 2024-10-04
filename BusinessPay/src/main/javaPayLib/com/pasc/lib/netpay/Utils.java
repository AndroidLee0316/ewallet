package com.pasc.lib.netpay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by huanglihou519 on 2018/9/2.
 */
public final class Utils {
  private static final String TAG = "PascNet";

  private Utils() {

  }

  public static boolean isNetworkConnected(Context context) {
    try {
      android.net.ConnectivityManager e =
              (android.net.ConnectivityManager) context.getSystemService(
                      Context.CONNECTIVITY_SERVICE);
      @SuppressLint("MissingPermission") NetworkInfo activeNetwork = e.getActiveNetworkInfo();
      return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    } catch (Exception e) {
      Log.w(TAG, e.toString());
    }

    return false;
  }

}
