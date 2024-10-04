package com.pasc.business.ewallet.common.filter;

import android.text.Spanned;

import com.pasc.business.ewallet.NotProguard;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
@NotProguard
public class BankCardInputFilter extends BaseInputFilter {
    int limit=23;//包括空格的长度
    public BankCardInputFilter(){

    }
    public BankCardInputFilter(int limit) {
        this.limit = limit;
    }
    @Override
    public int limitLength() {
        return limit;
    }
    /**
     * 当缓冲区要使用source的[start - end)范围内的内容替换dest的[dstart - dend)范围内的内容时调用该方法。
     * 返回值是你最终想要添加的文本。如果返回空字符""，则不添加新内容。如果返回空(null)，则添加本次输入的全部内容(即source)
     * 当你在删除已有文本时，source的长度为0。不要以为是错误而过滤这种清空。
     * 不要直接修改dest的内容，它的内容只是用来查看的。
     *
     * @param source 输入的文字
     * @param start 输入-0，删除-0
     * @param end 输入-文字的长度，删除-0
     * @param dest 原先显示的内容
     * @param dstart 输入-原光标位置，删除-光标删除结束位置
     * @param dend  输入-原光标位置，删除-光标删除开始位置
     * @return
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //输入框粘贴的规则如下，身份证 手机号 银行卡号 粘贴时如果有中文，
        // 就沾不进去,过长也沾不进去,切到中间粘贴如果沾上之后超过限定位数的话也沾不上去
        //Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
        String origin = dest.toString () + source.toString ();
        String replace = origin.replace (" ", "");

        Pattern p = Pattern.compile (regex ());//只能匹配数字
        Matcher m = p.matcher (replace);
        if (!m.matches ()) {
            return "";
        }

        if (origin.length () > max ()) {
            //粘贴超过字数，返回
            return "";
        } else if (origin.length () == max ()) {
            if (start == origin.length () - 1) {
                //刚好输入第24个的时候，用空格裁剪掉，后面会格式化
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
