package com.pasc.business.ewallet.common.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public abstract class BaseInputFilter implements InputFilter {

    // 最大长度
    public abstract int limitLength();

    public abstract String regex();

    protected int max() {
        return limitLength () + 1;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String origin = dest.toString () + source.toString ();
        if (TextUtils.isEmpty (origin)) {
            return "";
        }
        Pattern p = Pattern.compile (regex ());//只能匹配数，还有X
        Matcher m = p.matcher (origin);
        if (!m.matches ()) {
            return "";
        }
        if (origin.length () > max ()) {
            //粘贴超过字数，返回
            return "";
        } else if (origin.length () == max ()) {
            if (start == origin.length () - 1) {
                //刚好输入第19个的时候，用空格裁剪掉，后面会格式化
                return " ";
            } else {
                return " ";
            }

        }
        return null;

    }
}
