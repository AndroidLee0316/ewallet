package com.pasc.business.ewallet.common.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.pasc.business.ewallet.common.utils.TypeFaceUtil;

/**
 * @date 2019-09-05
 * @des
 * @modify
 **/
public class FontTextView extends AppCompatTextView {
    public FontTextView(Context context) {
        super (context);
        init (context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super (context, attrs);
        init (context);

    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context);
    }

    void init(Context context){
        Typeface typeface = TypeFaceUtil.getDinAlternateBold (context);
        if (typeface != null) {
            setTypeface (typeface);
        }
    }
}
