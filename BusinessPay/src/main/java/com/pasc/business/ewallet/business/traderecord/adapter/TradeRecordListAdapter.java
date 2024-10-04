package com.pasc.business.ewallet.business.traderecord.adapter;

import android.support.annotation.Nullable;

import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.adapter.base.BaseViewHolder;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceBean;

import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class TradeRecordListAdapter extends BaseQuickAdapter<AvailBalanceBean, BaseViewHolder> {
    public TradeRecordListAdapter (@Nullable List<AvailBalanceBean> data) {
        super(R.layout.ewallet_pay_yue_record_item, data);
    }

    @Override
    protected void convert (BaseViewHolder helper, AvailBalanceBean item) {
        helper.setText(R.id.ewallet_pay_yue_record_tv_type, item.remark)
                .setText(R.id.ewallet_pay_yue_record_tv_time, item.tranTime)
                .setText(R.id.ewallet_pay_yue_record_tv_money, item.getTradeAmount())
                .setVisible(R.id.ewallet_pay_yue_record_bill_divider, !item.isLastInIt)
                .setGone(R.id.ewallet_pay_date_tv_date, item.isHeader)
                .setText (R.id.ewallet_pay_date_tv_date,item.headerValue)
                .addOnClickListener (R.id.ewallet_pay_yue_record_ll_bill_root);

    }
}
