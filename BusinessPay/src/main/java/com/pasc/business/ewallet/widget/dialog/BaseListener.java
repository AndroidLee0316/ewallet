package com.pasc.business.ewallet.widget.dialog;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseListener implements Parcelable {

    public BaseListener() {
    }

    public BaseListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseListener> CREATOR = new Creator<BaseListener>() {
        @Override
        public BaseListener createFromParcel(Parcel in) {
            return new BaseListener(in);
        }

        @Override
        public BaseListener[] newArray(int size) {
            return new BaseListener[size];
        }
    };
}
