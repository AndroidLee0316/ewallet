package com.pasc.ewallet.dev.pay.unionpay.adapter;

import android.support.annotation.Nullable;
import com.pasc.ewallet.dev.R;
import com.pasc.ewallet.dev.pay.unionpay.model.BankCard;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Created by zhuangjiguang on 2021/3/8.
 */
public class BankCardAdapter extends BaseQuickAdapter<BankCard, BaseViewHolder> {
  private int selection = -1;
  private List<BankCard> bankCardList;
  public BankCardAdapter(@Nullable List<BankCard> data) {
    super(R.layout.ewallet_bank_card_list_item, data);
    bankCardList = data;
  }

  public void setSelection(int selection) {
    this.selection = selection;
    notifyDataSetChanged ();
  }

  @Override protected void convert(BaseViewHolder helper, BankCard item, int position) {
    super.convert(helper, item, position);
    BankCard bankCard = bankCardList.get(position);
    helper.setImageResource(R.id.ewallet_bank_card_select_iv, selection == position
        ? R.drawable.ewallet_bank_card_check : R.drawable.ewallet_bank_card_uncheck);
    helper.setText(R.id.ewallet_bank_card_title_tv,
        mContext.getResources().getString(R.string.ewallet_bank_card_list_item,
        bankCard.bankName, bankCard.cardNo.substring(bankCard.cardNo.length() - 4)));
  }

}
