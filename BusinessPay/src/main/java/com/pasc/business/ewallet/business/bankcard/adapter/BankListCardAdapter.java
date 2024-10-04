package com.pasc.business.ewallet.business.bankcard.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.lib.adapter.listview.BaseHolder;
import com.pasc.lib.adapter.listview.ListBaseAdapter;

import java.util.List;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class BankListCardAdapter<T extends IBankCardItem> extends ListBaseAdapter<T, BankListCardAdapter.BankCardHolder> {

    public interface BankCardListener {
        void bankClick(IBankCardItem bankCardItem);
    }

    private BankCardListener bankCardListener;

    public void setBankCardListener(BankCardListener bankCardListener) {
        this.bankCardListener = bankCardListener;
    }

    public BankListCardAdapter(Context context, List<T> data) {
        super (context, data);
    }

    @Override
    public int layout() {
        return R.layout.ewallet_bankcard_list_item;
    }

    @Override
    public BankCardHolder createBaseHolder(View rootView) {
        return new BankCardHolder (rootView);
    }

    @Override
    public void setData(BankCardHolder holder, IBankCardItem data, int position) {

        GlideUtil.loadImage (context, holder.ewalletBankListItemLogo,
                data.logo (),
                R.drawable.ewallet_ic_no_bank_card, R.drawable.ewallet_ic_no_bank_card);

        //目前没有水印，背景自带了
        if (Util.isEmpty (data.bankWaterMark ())){
            holder.ewalletBankListItemWaterMark.setVisibility (View.GONE);
        }else {
            holder.ewalletBankListItemWaterMark.setVisibility (View.VISIBLE);
            GlideUtil.loadImage (context, holder.ewalletBankListItemWaterMark,
                    data.bankWaterMark (),
                    R.drawable.ewallet_ic_bank_card_bg_logo, R.drawable.ewallet_ic_bank_card_bg_logo);
        }

        GlideUtil.loadImage (context, holder.ewalletBankListItemBg,
                data.bankBackground (),
                R.drawable.ewallet_bg_no_bank_card, R.drawable.ewallet_bg_no_bank_card);

        holder.ewalletBankListItemName.setText (data.bankName ());
        holder.ewalletBankListItemType.setText (data.cardTypeName ());
        holder.ewalletBankListItemCardno.setText (data.cardNo ());

        holder.ewallet_card_root.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (bankCardListener != null) {
                    bankCardListener.bankClick (data);
                }
            }
        });

    }

    public static class BankCardHolder extends BaseHolder {
        public BankCardHolder(View rootView) {
            super (rootView);
            initView ();
        }

        ImageView ewalletBankListItemBg;
        ImageView ewalletBankListItemLogo;
        TextView ewalletBankListItemName;
        TextView ewalletBankListItemType;
        TextView ewalletBankListItemCardno;
        ImageView ewalletBankListItemWaterMark;
        View ewallet_card_root;

        void initView() {
            ewallet_card_root = findViewById (R.id.ewallet_card_root);
            ewalletBankListItemBg = findViewById (R.id.ewallet_bank_list_item_bg);
            ewalletBankListItemLogo = findViewById (R.id.ewallet_bank_list_item_logo);
            ewalletBankListItemName = findViewById (R.id.ewallet_bank_list_item_name);
            ewalletBankListItemType = findViewById (R.id.ewallet_bank_list_item_type);
            ewalletBankListItemCardno = findViewById (R.id.ewallet_bank_list_item_cardno);
            ewalletBankListItemWaterMark = findViewById (R.id.ewallet_bank_list_item_water_mark);
        }

    }
}
