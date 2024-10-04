package com.pasc.lib.keyboard;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pasc.business.ewallet.R;

import java.util.ArrayList;

/**
 * @author yangzijian
 * @date 2019/2/14
 * @des
 * @modify
 **/
public class EwalletKeyBoardAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<String> valueList;
    private boolean isEnable;
    public void setEnable(boolean isEnable){
        this.isEnable=isEnable;
    }

    public EwalletKeyBoardAdapter(Context mContext, ArrayList<String> valueList) {
        this.mContext = mContext;
        this.valueList = valueList;
    }

    @Override
    public int getCount() {
        return valueList.size();
    }

    @Override
    public Object getItem(int position) {
        return valueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.ewallet_grid_item_virtual_keyboard, null);
            viewHolder = new ViewHolder();
            viewHolder.btnKey = convertView.findViewById(R.id.btn_keys);
            viewHolder.imgDelete = convertView.findViewById(R.id.imgDelete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 9) {

            viewHolder.imgDelete.setVisibility(View.INVISIBLE);
            viewHolder.btnKey.setVisibility(View.VISIBLE);
            viewHolder.btnKey.setText(valueList.get(position));
            if (isEnable){
                viewHolder.btnKey.setBackgroundColor(Color.parseColor("#E8E8E8"));
            }
        } else if (position == 11) {
            viewHolder.btnKey.setBackgroundResource(R.drawable.ewallet_keyborad_delete);
            viewHolder.imgDelete.setVisibility(View.VISIBLE);
            viewHolder.btnKey.setVisibility(View.INVISIBLE);

        } else {
            viewHolder.imgDelete.setVisibility(View.INVISIBLE);
            viewHolder.btnKey.setVisibility(View.VISIBLE);

            viewHolder.btnKey.setText(valueList.get(position));
        }

        return convertView;
    }

    public final class ViewHolder {
        public TextView btnKey;
        public RelativeLayout imgDelete;
    }
}
