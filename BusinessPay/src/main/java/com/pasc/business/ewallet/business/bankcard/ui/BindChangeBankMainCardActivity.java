package com.pasc.business.ewallet.business.bankcard.ui;

import android.annotation.SuppressLint;
import android.view.View;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.widget.dialognt.CommonDialog;

/****
 * 换绑 安全卡/主卡
 */
public class BindChangeBankMainCardActivity extends AddMainCardActivity {

    @Override
    protected void initToolBar() {
        toolbar.setTitle (getString (R.string.ewallet_exchange_safe_bank_card));
        toolbar.enableUnderDivider (false);
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                exitTip ();
            }
        });
        test ();
    }


    int test() {
        int a = R.layout.ewallet_activity_account_add_card_more;
        int b = R.layout.ewallet_activity_account_add_card_info;

        return a + b;
    }

    @Override
    public void onBackPressed() {
        exitTip ();
    }


    @Override
    public void bindCardSuccess() {
        RouterManager.BankCardRouter.gotoAddMainCardPhoneOtp (this, bankCardNum, phoneNum, true);
    }

    @SuppressLint("WrongConstant")
    void exitTip() {
        CommonDialog commonDialog = new CommonDialog (getActivity ());
        commonDialog.setTitle (getString (R.string.ewallet_exchange_is_give_up_change))
                .setButton1 (getString (R.string.ewallet_exchange_give_up_change), "#22C8D8")
                .setButton2 (getString (R.string.ewallet_cancel))
                .setOnButtonClickListener (new CommonDialog.OnButtonClickListener () {
                    @Override
                    public void button1Click() {
                        super.button1Click ();
                        finish ();
                    }

                    @Override
                    public void button2Click() {
                        super.button2Click ();
                    }
                })
                .show ();
    }
}
