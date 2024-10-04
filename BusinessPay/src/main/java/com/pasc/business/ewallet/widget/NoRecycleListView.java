package com.pasc.business.ewallet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify
 **/
public class NoRecycleListView extends ListView {
    public NoRecycleListView(Context context) {
        super (context);
    }

    public NoRecycleListView(Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public NoRecycleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Integer.MAX_VALUE:表示int类型能够表示的最大值，值为2的31次方-1
        //>>2:右移N位相当于除以2的N的幂
        //MeasureSpec.AT_MOST：子布局可以根据自己的大小选择任意大小的模式
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

}
