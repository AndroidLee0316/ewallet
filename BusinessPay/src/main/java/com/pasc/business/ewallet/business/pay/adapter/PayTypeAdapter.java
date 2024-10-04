package com.pasc.business.ewallet.business.pay.adapter;

import android.os.Build;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.pay.net.resp.PayTypeBean;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date 2019/7/30
 * @des
 * @modify
 **/
public class PayTypeAdapter extends BaseQuickAdapter<PayTypeBean, BaseViewHolder> {
    private int selection = 0;

    public void setSelection(int selection) {
        this.selection = selection;
        notifyDataSetChanged ();
    }

    private boolean showSelectIcon;

    public PayTypeAdapter(@Nullable List<PayTypeBean> data, boolean showSelectIcon) {
        super (R.layout.ewallet_type_list_item, data);
        this.showSelectIcon = showSelectIcon;
    }

    @Override
    protected void convert(BaseViewHolder helper, PayTypeBean item, int position) {
        ImageView iv = helper.getView (R.id.ewallet_type_icon_iv);
        GlideUtil.loadImage (iv.getContext (), iv, item.icon, item.defaultIcon (), item.defaultIcon ());
        //helper.setVisible (R.id.ewallet_pay_type_select_iv, showSelectIcon && !item.disable)
        //        .setVisible (R.id.ewallet_pay_type_arrow, !showSelectIcon)
        //        .setText (R.id.ewallet_pay_type_title_tv, item.getPayTypeName ())
        //        .setImageResource (R.id.ewallet_pay_type_select_iv, selection == position
        //                ? R.drawable.ewallet_circle_check : R.drawable.ewallet_circle_uncheck);
        if (StatusTable.PayType.SELECT_MORE.equalsIgnoreCase(item.payType)) {
            helper.setVisible (R.id.ewallet_pay_type_select_iv, false)
                .setVisible (R.id.ewallet_pay_type_arrow, true)
                .setText (R.id.ewallet_pay_type_title_tv, item.getPayTypeName ())
                .setImageResource (R.id.ewallet_pay_type_select_iv, selection == position
                    ? R.drawable.ewallet_circle_check : R.drawable.ewallet_circle_uncheck);
        } else {
            helper.setVisible (R.id.ewallet_pay_type_select_iv, showSelectIcon)
                .setVisible (R.id.ewallet_pay_type_arrow, !showSelectIcon)
                .setText (R.id.ewallet_pay_type_title_tv, item.getPayTypeName ())
                .setImageResource (R.id.ewallet_pay_type_select_iv, selection == position
                    ? R.drawable.ewallet_circle_check : R.drawable.ewallet_circle_uncheck);
        }

        String desc = null;
        String title = null;

        if (item.discountContext != null) {
            desc = item.discountContext.desc;
            title = item.discountContext.title;
        }

        helper.setGone (R.id.ewallet_pay_type_remind, !Util.isEmpty (desc))
//                .setGone (R.id.ewallet_pay_type_remind2, !Util.isEmpty (title))
                .setGone (R.id.ewallet_pay_type_recommend,!Util.isEmpty (title))
                .setText (R.id.ewallet_pay_type_recommend,title)
                .setText (R.id.ewallet_pay_type_remind, desc)
//                .setText (R.id.ewallet_pay_type_remind2, title)
                .setGone (R.id.ewallet_pay_type_subtitle_ll, !Util.isEmpty (desc) /***|| !Util.isEmpty (title)***/)
                .setGone (R.id.ewallet_type_list_shade, item.disable)
        ;

        if (item.disable) {
            if (Build.VERSION.SDK_INT >= 16) {
                helper.itemView.setBackground (null);
            } else {
                helper.itemView.setBackgroundDrawable (null);
            }
            if (StatusTable.PayType.BALANCE.equalsIgnoreCase (item.payType)) {
                helper.setVisible (R.id.ewallet_pay_type_remind, true);
                helper .setVisible (R.id.ewallet_pay_type_subtitle_ll,true);
                helper.setText (R.id.ewallet_pay_type_remind, "钱包余额不足");

            }

        } else {
            helper.itemView.setBackgroundResource (R.drawable.ewallet_btn_default_white_selector);

        }
    }
}
