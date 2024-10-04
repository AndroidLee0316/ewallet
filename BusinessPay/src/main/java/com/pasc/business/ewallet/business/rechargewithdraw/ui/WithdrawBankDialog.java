package com.pasc.business.ewallet.business.rechargewithdraw.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.bankcard.adapter.BankSelectAdapter;
import com.pasc.business.ewallet.business.bankcard.adapter.IBankCardItem;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;

import java.util.List;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
public class WithdrawBankDialog extends Dialog {

    private PascToolbar mToolbar;
    private ListView mListView;
    private BankSelectAdapter bankSelectAdapter;
    private AdapterView.OnItemClickListener itemClickListener;

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public WithdrawBankDialog(@NonNull Context context, List<? extends IBankCardItem> data, String title) {
        super (context, R.style.EwalletBottomDialogStyle);
        View view = LayoutInflater.from (context).inflate (R.layout.ewallet_bank_card_select_view, null);
        setContentView (view);
        mToolbar = view.findViewById (R.id.ewallet_bank_card_select_toolbar);
        mListView = view.findViewById (R.id.ewallet_bank_card_select_lv);
        mToolbar.setTitle(title);
        setCanceledOnTouchOutside (true);
        mToolbar.addLeftImageButton (R.drawable.ewallet_close_icon).setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dismiss ();
            }
        });
        bankSelectAdapter=new BankSelectAdapter (context,data);
        mListView.setAdapter (bankSelectAdapter);

        mListView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemClickListener!=null){
                    itemClickListener.onItemClick (parent,view,position,id);
                }
                dismiss ();
            }
        });

    }


    @Override
    public void show() {
        super.show ();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
//        WindowManager.LayoutParams layoutParams = getWindow ().getAttributes ();
//        layoutParams.gravity = Gravity.BOTTOM;
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        getWindow ().getDecorView ().setPadding (0, 0, 0, 0);
//
//        getWindow ().setAttributes (layoutParams);
    }
}
