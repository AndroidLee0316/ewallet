package com.pasc.lib.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.pasc.business.ewallet.NotProguard;
import java.util.List;

/**
 * @author yangzijian
 * @date 2019/2/26
 * @des
 * @modify
 **/
@NotProguard
public abstract class ListBaseAdapter<T, H extends BaseHolder> extends BaseAdapter {
    public List<T> data;
    public Context context;

    public ListBaseAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size ();
    }

    @Override
    public Object getItem(int position) {
        return data.get (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H baseHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from (context).inflate (layout (), null);
            baseHolder = createBaseHolder (convertView);
        } else {
            baseHolder = (H) convertView.getTag ();
        }
        setData (baseHolder, data.get (position),position);
        return convertView;
    }

    public abstract int layout();

    public abstract H createBaseHolder(View rootView);


    public abstract void setData(H holder, T data,int position);


}
