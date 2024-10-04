package com.pasc.business.ewallet.business.bankcard.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.lib.adapter.listview.BaseHolder;
import com.pasc.lib.adapter.listview.ListBaseAdapter;

import java.util.List;

/**
 * @date 2019/8/17
 * @des
 * @modify
 **/
public class BankSelectAdapter<T extends IBankCardItem> extends ListBaseAdapter<T, BankSelectAdapter.BankSelectHolder> {


    public BankSelectAdapter(Context context, List<T> data) {
        super (context, data);
    }

    @Override
    public int layout() {
        return R.layout.ewallet_item_bank_card_list;
    }

    @Override
    public BankSelectHolder createBaseHolder(View rootView) {
        return new BankSelectHolder (rootView);
    }

    @Override
    public void setData(BankSelectHolder holder, T data, int position) {
        holder.ewalletItemBankCardListTitleTv.setText (data.bankName ());
        GlideUtil.loadImage (context, holder.ewalletItemBankCardListIconIv,
                data.logo (),
                R.drawable.ewallet_ic_no_bank_card, R.drawable.ewallet_ic_no_bank_card);
    }

    public static class BankSelectHolder extends BaseHolder {
        public BankSelectHolder(View rootView) {
            super (rootView);
            initView ();
        }

        private ImageView ewalletItemBankCardListIconIv;
        private TextView ewalletItemBankCardListTitleTv;
        private TextView ewalletItemBankCardListSubtitleTv;

        private void initView() {
            ewalletItemBankCardListIconIv = findViewById (R.id.ewallet_item_bank_card_list_icon_iv);
            ewalletItemBankCardListTitleTv = findViewById (R.id.ewallet_item_bank_card_list_title_tv);
            ewalletItemBankCardListSubtitleTv = findViewById (R.id.ewallet_item_bank_card_list_subtitle_tv);
        }


    }
}
