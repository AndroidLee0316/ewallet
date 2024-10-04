package com.pasc.business.ewallet.common.filter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.pasc.business.ewallet.business.util.AccountUtil;

/**
 * @date 2019/8/15
 * @des
 * @modify
 **/
public class MoneyInputFilter {
    private CharSequence inputNum = "";

    public interface MoneyInputListener {
        void inputEmpty();
        void isZero();
        void outputMoney(double money);
    }

    public void listenInput(EditText editText, MoneyInputListener inputListener) {
        editText.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals (s.toString ())) {
                    inputListener.inputEmpty ();
                    return;
                } else {
                    if (AccountUtil.checkAndAddPreInputMoneyOnlyDot (s)) {//检测输入内容是否只有一个点，是的话需要主动在前面加一个0
                        s = "0" + s;
                        inputNum = s;
                        editText.setText (s);
                        editText.setSelection (s.length ()); //光标移到最后
                        inputListener.isZero ();
                        return;
                    }
                    if (AccountUtil.isInputMoneyExceedMax (s.toString ())) {// 检测输入的位数是否超过最大的位数,是的话需要设置为旧的输入数据
                        if (count > 1) {//非手动输入
                            editText.setText (s.toString ().substring (0, AccountUtil.INPUT_MONEY_NUM_BITS_MAX));
                            editText.setSelection (editText.getText ().toString ().length ()); //光标最后
                        } else {
                            editText.setText (inputNum);
                            editText.setSelection (start); //光标不动
                        }
                        double moneyNum = AccountUtil.formatInputMoneyNum (editText.getText ().toString ());
                        if (moneyNum == 0) {
                            inputListener.isZero ();
                        } else {
                            inputListener.outputMoney (moneyNum);
                        }
                        return;
                    } else if (AccountUtil.isInputMoneyExceedBits (s.toString ())) {//且小数点后是否超过两位数，是的话需要设置为旧的输入数据
                        if (count > 1) {//非手动输入
                            String result = String.format ("%.2f", Double.valueOf (s.toString ()));
                            editText.setText (result);
                            editText.setSelection (editText.getText ().toString ().length ()); //光标最后
                        } else {
                            editText.setText (inputNum);
                            editText.setSelection (start); //光标不动
                        }
                        double moneyNum = AccountUtil.formatInputMoneyNum (editText.getText ().toString ());
                        if (moneyNum == 0) {
                            inputListener.isZero ();
                        } else {
                            inputListener.outputMoney (moneyNum);
                        }
                        return;
                    }

                    //如果非手动输入，光标在最后面
                    if (count > 1) {
                        editText.setSelection (editText.getText ().toString ().length ()); //光标不动
                    }
                }
                inputNum = s.toString ();
                double moneyNum = AccountUtil.formatInputMoneyNum (editText.getText ().toString ());
                if (moneyNum == 0) {
                    inputListener.isZero ();
                } else {
                    inputListener.outputMoney (moneyNum);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
