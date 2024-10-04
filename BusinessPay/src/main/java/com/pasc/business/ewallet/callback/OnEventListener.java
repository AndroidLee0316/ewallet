package com.pasc.business.ewallet.callback;

import android.content.Context;

import com.pasc.business.ewallet.NotProguard;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * @date 2019-08-30
 * @des
 * @modify
 **/
@NotProguard
public abstract class OnEventListener {

    public abstract void onEvent(@NonNull String eventID, @Nullable String label, @Nullable Map<String, String> map);

    public void onPageStart(String pageName) {
    }

    public void onPageEnd(String pageName) {
    }


    public void onResume(Context context) {
    }


    public void onPause(Context context) {
    }
}
