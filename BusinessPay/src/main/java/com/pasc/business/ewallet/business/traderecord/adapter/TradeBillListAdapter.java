package com.pasc.business.ewallet.business.traderecord.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.traderecord.net.resp.BillBean;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author yangzijian
 * @date 2019/3/21
 * @des
 * @modify
 **/
public class TradeBillListAdapter extends BaseQuickAdapter<BillBean, BaseViewHolder> {
    private Context context;
    public TradeBillListAdapter(Context context, @Nullable List<BillBean> data) {
        super (R.layout.ewallet_pay_bill_record_item, data);
        this.context=context;
    }
    private boolean needShowHeader=true;

    public void setNeedShowHeader(boolean needShowHeader) {
        this.needShowHeader = needShowHeader;
    }

    @Override
    protected void convert(BaseViewHolder helper, BillBean item) {
        helper.setGone (R.id.ewallet_pay_bill_ll,item.isHeader && needShowHeader)
         .setText (R.id.ewallet_pay_date_tv_date,item.getHeaderValue ())
                .setText (R.id.ewallet_pay_bill_record_tv_type, item.goodsName)
                .setText (R.id.ewallet_pay_bill_record_tv_time, item.getTime ())
                .setText (R.id.ewallet_pay_bill_record_tv_money, item.getPayAmount ())
                .setVisible(R.id.ewallet_pay_bill_record_bill_divider, !item.isLastInIt)
        .addOnClickListener (R.id.ewallet_pay_bill_record_ll_bill_root)
        .addOnClickListener (R.id.ewallet_pay_month_bill_tv)
        ;
        ImageView imageView=helper.getView (R.id.ewallet_pay_bill_record_iv_icon);
//        helper.setVisible (R.id.ewallet_pay_bill_record_bill_tv_status,item.isOnLinePay ())
//         .setText (R.id.ewallet_pay_bill_record_bill_tv_status,item.getStatusText ());
        int DefaultIcon=item.getDefaultIcon ();
        GlideUtil.loadImage (context,imageView,item.merchantIcon,DefaultIcon,DefaultIcon);
    }
}
