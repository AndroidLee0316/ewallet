package com.pasc.business.ewallet.business.bankcard.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.pasc.business.ewallet.R;

/**
 * @date 2019-08-27
 * @des
 * @modify
 **/
public class SafeCardDialog extends Dialog {
    public SafeCardDialog(@NonNull Context context) {
        super(context, R.style.EwalletRoundDialog);
        View view=View.inflate (context,R.layout.ewallet_safe_card_dialog,null);
        setContentView (view);

        view.findViewById (R.id.tv_button2).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dismiss ();
            }
        });

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    @Override
    public void show() {
        super.show ();
    }
}
