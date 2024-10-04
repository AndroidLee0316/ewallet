package com.pasc.business.ewallet.business.home.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.home.bean.HomeItemBean;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.DensityUtils;
import com.pasc.lib.adapter.listview.BaseHolder;
import com.pasc.lib.adapter.listview.ListBaseAdapter;

import java.util.List;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class HomeAdapter extends ListBaseAdapter<HomeItemBean, HomeAdapter.HomeHolder> {


    public HomeAdapter(Context context, List<HomeItemBean> data) {
        super (context, data);
    }

    @Override
    public int layout() {
        return R.layout.ewallet_home_adapter_item;
    }

    @Override
    public HomeHolder createBaseHolder(View rootView) {
        return new HomeHolder (rootView);
    }

    int dp2Px(int dp){
       return DensityUtils.dip2px (context,dp);
    }
    @Override
    public void setData(HomeHolder holder, HomeItemBean data,int position) {
        holder.ewalletHomeIconIv.setImageResource (data.getIcon ());
        holder.ewalletHomeTextTv.setText (data.getText ());
        holder.ewalletHomeIconBgIv.setImageResource (data.getBgIcon ());
        if (data.getBg () > 0) {
            holder.item.setPadding (0,dp2Px (20),0,0);
            holder.bg.setBackgroundResource (data.getBg ());
        } else {
            holder.item.setPadding (0,0,0,0);
            holder.bg.setBackgroundColor (context.getResources ().getColor (R.color.ewallet_white));
            if (Build.VERSION.SDK_INT>=16){
                holder.bg.setBackground (null);
            }else {
                holder.bg.setBackgroundDrawable (null);

            }
        }

    }

    public static class HomeHolder extends BaseHolder {
        public ImageView ewalletHomeIconIv;
        public TextView ewalletHomeTextTv;
        public ImageView ewalletHomeIconBgIv;
        public View  bg,item;

        private void initView() {
            ewalletHomeIconIv = findViewById (R.id.ewallet_home_icon_iv);
            ewalletHomeTextTv = findViewById (R.id.ewallet_home_text_tv);
            ewalletHomeIconBgIv = findViewById (R.id.ewallet_home_iconBg_iv);
            item = findViewById (R.id.ewallet_home_item_ll);
            bg = findViewById (R.id.ewallet_home_bg_ll);

        }

        public HomeHolder(View rootView) {
            super (rootView);
            initView ();
        }
    }
}
