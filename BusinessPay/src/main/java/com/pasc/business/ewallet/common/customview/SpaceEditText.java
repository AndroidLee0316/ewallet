package com.pasc.business.ewallet.common.customview;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.common.utils.Util;
@NotProguard
public class SpaceEditText extends AppCompatEditText {
    //上次输入框中的内容
    private String lastString = "";
    //光标的位置
//    private int selectPosition;
    //输入框内容改变监听
    private TextChangeListener listener;
    private int[] formatInts = new int[]{};

    public SpaceEditText (Context context) {
        super(context, null);
    }

    public SpaceEditText (Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SpaceEditText (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *  输入格式化参数，比如 4，4，4，4 类型格式化银行卡，输出为1234 5678 1234 5678.
     *  默认是不格式化.
     * @param ints
     */
    public void format(int... ints){
        if (ints != null){
            this.formatInts = ints;
        }
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            /**
             * 当输入框内容改变时的回调
             * @param s  改变后的字符串
             * @param start 改变之后的光标下标
             * @param before 删除了多少个字符
             * @param count 添加了多少个字符
             */
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (formatInts.length == 0){
                    return;
                }
                //因为重新排序之后setText的存在
                //会导致输入框的内容从0开始输入，这里是为了避免这种情况产生一系列问题
                if (start == 0 && count > 1 && getSelectionStart() == 0){
                    return;
                }

                String textTrim = getText().toString().replaceAll("\\s", "");
                if (TextUtils.isEmpty(textTrim)){
                    return;
                }
                //如果 before >0 && count == 0,代表此次操作是删除操作
                if (before > 0 && count == 0){
//                    selectPosition = start;
                } else{
                    //此处代表是添加操作
                    //当光标位于空格之前，添加字符时，需要让光标跳过空格，再按照之前的逻辑计算光标位置
                    int c = start + count;
//                    selectPosition = c;
                    int offset = 0;
                    int[] ints = Util.formatIntegerSpace(formatInts);

                    //判断位置
                    //去除最后一个
                    for (int i = 0; i < ints.length - 1; i++){
                        int p = ints[i] + i + 1;
                        if (c == p){
                            offset = 1;
                            break;
                        }
                    }
//                    selectPosition += offset;
                }
            }


            @Override
            public void afterTextChanged (Editable s) {
                if (formatInts.length == 0){
                    //触发回调内容
                    if (listener != null){
                        listener.textChange(s.toString());
                    }
                    return;
                }

                //获取输入框中的内容,不可以去空格
                String etContent = getText().toString();
                if (TextUtils.isEmpty(etContent)){
                    if (listener != null){
                        listener.textChange("");
                    }
                    return;
                }
                //重新拼接字符串
                String newContent = Util.addTextSpace(etContent, formatInts);
                //防止调用lastString的引用
                String temp = new StringBuilder().append(lastString).toString();

                //保存本次字符串数据
                lastString = newContent;

                //如果有改变，则重新填充
                //防止EditText无限setText()产生死循环
                if (!temp.equals(etContent)){
                    //可能出现递归调用
                    setText(newContent);
                    //保证光标的位置
//                    setSelection(selectPosition > newContent.length() ? newContent.length() : selectPosition);
                }
                try {
                    setSelection(newContent.length());
                }catch (Exception e){
                    e.printStackTrace ();
                }
                //触发回调内容
                if (listener != null){

                    listener.textChange(newContent);
                }

            }
        });
    }

    /**
     *  删除最大限制字数后面的空格，不能和format(int... ints)一起使用；
     *  @param lastPosition 包含空格
     */
    public void delLastSpace(int lastPosition){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged (Editable s) {
                String s1 = s.toString();
                if (s1.length() >= lastPosition){
                    String substring = s1.substring(s1.length() - 1, s1.length());
                    if (" ".equals(substring)){
                        //处理空格
                        setText(s1.substring(0, s1.length() - 1));
                        setSelection(s1.length() - 1);
                    }else {
                        int i = s1.indexOf(" ");
                        //获取空格位置
                        String replace = s1.replace(" ", "");
                        setText(replace);
                        if (i > 0){
                            setSelection(i-1);
                        }else {
                            setSelection(i);
                        }
                    }
                }else {
                    //触发回调内容
                    if (listener != null){
                        listener.textChange(s1);
                    }
                }
            }
        });
    }
    /**
     * 输入框内容回调，当输入框内容改变时会触发
     */
    @NotProguard
    public interface TextChangeListener {
        void textChange (String text);
    }

    public void setTextChangeListener (TextChangeListener listener) {
        this.listener = listener;

    }
}
