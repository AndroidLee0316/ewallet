package com.pasc.business.ewallet.common.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.ref.WeakReference;

/**
 * 功能：字体工具类
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/3/5
 */
public class TypeFaceUtil {

    private static WeakReference<Typeface> fontCache = null;

    /**
     * 通过字体名称获取字体
     * @param context
     * @param fontname
     * @return
     */
    public static Typeface getTypeface(Context context, String fontname) {
        if (fontCache != null && fontCache.get() != null){
            return fontCache.get();
        }

        try {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            fontCache = new WeakReference<>(typeface);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return fontCache.get();
    }

    /**
     * 获取DIN-Medium字体
     * @param context
     * @return
     */
    public static Typeface getDinAlternateBold(Context context){
        return getTypeface(context,"fonts/DIN-Medium.otf");
    }

}
