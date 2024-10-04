package com.pasc.lib.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.common.utils.KeyBoardUtils;

import java.lang.reflect.Method;

/**
 * @author yangzijian
 * @date 2019/2/14
 * @des
 * @modify  密码虚拟键盘
 **/
public class EwalletKeyboardExtraView extends FrameLayout {

    Context context;
    private View layoutHide;
    private EditText editText;
    private EwalletKeyboardView keyboardView;
    private final static int moneyType = 0;
    private final static int idCardType = 1;
    private final static int bankCardType = 3;
    private final static int password = 4;
    private int duration=400;
    private  OnFocusChangeListener mOnFocusChangeListener;
    private  OnFocusChangeListener mOnFocusScrollChangeListener;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener (AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private int keyboardType = moneyType;

    public  void setFocusChangeListener(OnFocusChangeListener mOnFocusChangeListener){
        this.mOnFocusChangeListener=mOnFocusChangeListener;
    }

    public  void setFocusScrollChangeListener(OnFocusChangeListener mOnFocusScrollChangeListener){
        this.mOnFocusScrollChangeListener=mOnFocusScrollChangeListener;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private Runnable runnable=new Runnable () {
        @Override
        public void run() {
            show ();
        }
    };

    private Handler handler=new Handler () {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage (msg);
        }
    };

    public void setEditText(Activity activity, EditText editText) {
        if (editText != null) {
            this.editText = editText;
            // 设置不调用系统键盘
            if (android.os.Build.VERSION.SDK_INT <= 10) {
                editText.setInputType (InputType.TYPE_NULL);
            } else {
                if (activity != null) {
                    activity.getWindow ().setSoftInputMode (
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
                try {
                    Class<EditText> cls = EditText.class;
                    Method setShowSoftInputOnFocus;
                    setShowSoftInputOnFocus = cls.getMethod ("setShowSoftInputOnFocus",
                            boolean.class);
                    setShowSoftInputOnFocus.setAccessible (true);
                    setShowSoftInputOnFocus.invoke (editText, false);
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }


            editText.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {
                    show ();
                }
            });



            editText.setOnFocusChangeListener (new OnFocusChangeListener () {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mOnFocusScrollChangeListener != null){
                        mOnFocusScrollChangeListener.onFocusChange(v, hasFocus);
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run () {
                                handler.removeCallbacks (runnable);
                                if (hasFocus){
                                    KeyBoardUtils.hideInputForce (activity);
                                    handler.postDelayed (runnable,duration);
                                }else {
                                    hide ();
                                }
                                if (mOnFocusChangeListener!=null){
                                    mOnFocusChangeListener.onFocusChange(v,hasFocus);
                                }
                            }
                        }, 50);
                    } else{
                        handler.removeCallbacks (runnable);
//                      Log.d ("yzj",editText.getTag ().toString ()+"-"+hasFocus);
                        if (hasFocus){
                            KeyBoardUtils.hideInputForce (activity);
                            handler.postDelayed (runnable,duration);
                        }else {
                            hide ();
                        }
                        if (mOnFocusChangeListener!=null){
                            mOnFocusChangeListener.onFocusChange(v,hasFocus);
                        }
                    }
                }
            });
        }

    }
    public void show(){
        setVisibility (VISIBLE);
        keyboardView.show ();

    }

    public void showDownArrow(){
//        layoutHide.setVisibility (VISIBLE);

    }

    public void hide(){
        setVisibility (GONE);
        keyboardView.hide ();

    }

    public void hideDownArrow(){
        layoutHide.setVisibility (INVISIBLE);

    }

    public EwalletKeyboardExtraView(Context context) {
        this (context, null);
    }

    public EwalletKeyboardExtraView(Context context, AttributeSet attrs) {
        super (context, attrs);

        TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.ewalletKeyboardView);
        keyboardType = array.getInt (R.styleable.ewalletKeyboardView_ewalletKeyboardType, moneyType);
        array.recycle ();

        this.context = context;
        View view = View.inflate (context, R.layout.ewallet_layout_virtual_keyboard_extra, null);
        keyboardView = view.findViewById (R.id.keyboardView);
        keyboardView.setKeyboardType (keyboardType);
        layoutHide = keyboardView.findViewById (R.id.layoutHide);
        //layoutHide.setVisibility (VISIBLE);
        layoutHide.setVisibility (INVISIBLE);
        initListener ();
        addView (view);
        setVisibility (GONE);
    }


    private void initListener() {
        layoutHide.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                keyboardView.show ();
            }
        });

        keyboardView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(parent,view,position,id);
                    return;
                }
                if (editText == null) {
                    return;
                }
                if (position < 11 && position != 9) {
                    //点击0~9按钮
                    insertText(keyboardView.getValueList ().get (position));
                } else {
                    if (position == 9) {
                        //点击 . 空白 或者 X
                        String amount = editText.getText ().toString ().trim ();

                        if (keyboardType == moneyType) {
                            if (!amount.contains (".")) {
                                insertText(keyboardView.getValueList ().get (position));
                            }
                        } else {
                            insertText(keyboardView.getValueList ().get (position));
                        }
                    }

                    if (position == 11) {
                        //点击退格键
                        deleteText();
                    }
                }
            }
        });
    }

    private void insertText(String text){
        //获取光标所在位置
        int index = editText.getSelectionStart();
        //获取EditText的文字
        Editable edit = editText.getEditableText();
        if (index < 0 || index >= edit.length() ){
            //追加
            edit.append(text);
        }else{
            //光标所在位置插入文字
            edit.insert(index,text);
        }
    }

    private void deleteText (){
        int index = editText.getSelectionEnd();
        Editable edit = editText.getEditableText();
        if (edit.length() == 0){
            return;
        }
        if (index < 0 || index >= edit.length() ){
            edit.delete(edit.length()-1, edit.length());
        }else{
            //删除
            edit.delete(index -1 ,index);
            //editText.setSelection(edit.length());
        }
    }

}
