package com.pasc.business.ewallet.common.filter;

import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public class IdCardInputFilter extends BaseInputFilter {
    int limit=18;
    public IdCardInputFilter(){

    }
    public IdCardInputFilter(int limit) {
        this.limit = limit;
    }
    @Override
    public int limitLength() {
        return 18;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String origin = dest.toString () + source.toString ();
        if (source.toString ().contains (" ")){
            return "";
        }
        if (TextUtils.isEmpty (origin)) {
            return "";
        }


        boolean matchLen= (origin.replace (" ","").length ()==limitLength ()
               /** || origin.replace (" ","").length ()==15 **/);
        Pattern p = Pattern.compile (regex ());//只能匹配数，还有X
        Matcher m = p.matcher (origin);

        if (matchLen){
            String originPre=origin.substring (0,origin.length ()-1);
            Pattern p1 = Pattern.compile (regex ());//只能匹配数，还有X
            Matcher m1 = p1.matcher (originPre);

            String last=origin.substring (origin.length ()-1,origin.length ());
            if (!m1.matches ()) {
                return "";
            }else {
                if (!"X".equalsIgnoreCase (last) && !m.matches ()){
                    return "";
                }
            }
        }else {
            if (!m.matches ()) {
                return "";
            }
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

    @Override
    public String regex() {
        return "^[0-9]+$";
    }
}
