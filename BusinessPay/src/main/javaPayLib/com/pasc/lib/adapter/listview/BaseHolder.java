package com.pasc.lib.adapter.listview;

import android.view.View;
import com.pasc.business.ewallet.NotProguard;

/**
 * @author yangzijian
 * @date 2019/2/26
 * @des
 * @modify
 **/
@NotProguard
public class BaseHolder {
    public View rootView;
    public BaseHolder(View rootView) {
        this.rootView=rootView;
        rootView.setTag (this);
    }

    public <T extends View> T findViewById(int id){
        return rootView.findViewById (id);
    }
}
