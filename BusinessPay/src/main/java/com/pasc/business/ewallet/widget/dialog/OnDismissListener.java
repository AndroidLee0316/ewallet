package com.pasc.business.ewallet.widget.dialog;

import android.support.v4.app.DialogFragment;

public abstract class OnDismissListener<T extends DialogFragment> extends BaseListener {

    public abstract void onDismiss(T dialogFragment);
}
