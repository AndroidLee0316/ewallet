package com.pasc.business.ewallet.widget.dialog;

import android.support.v4.app.DialogFragment;

public abstract class OnConfirmListener<T extends DialogFragment> extends BaseListener{

    public abstract void onConfirm(T dialogFragment);
}
