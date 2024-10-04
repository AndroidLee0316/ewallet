package com.pasc.lib.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;


import com.pasc.business.ewallet.R;

import java.util.ArrayList;

/**
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify 密码虚拟键盘
 **/
public class EwalletKeyboardView extends FrameLayout {

    private Context context;
    private GridView gridView;
    private View layoutHide;
    View imgPwd, imgHide;
    private ArrayList<String> valueList = new ArrayList<> ();
    private final static int moneyType = 0;
    private final static int idCardType = 1;
    private final static int bankCardType = 3;
    private final static int password = 4;
    private Animation enterAnim;
    private Animation exitAnim;
    private int keyboardType = moneyType;
    boolean showSafe = false;
    private boolean canEnter = true;

    public void setCanEnter(boolean canEnter) {
        this.canEnter = canEnter;
    }

    private AdapterView.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public EwalletKeyboardView(Context context) {
        this (context, null);
    }

    public EwalletKeyboardView(Context context, AttributeSet attrs) {
        super (context, attrs);
        TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.ewalletKeyboardView);
        keyboardType = array.getInt (R.styleable.ewalletKeyboardView_ewalletKeyboardType, moneyType);
        showSafe = array.getBoolean (R.styleable.ewalletKeyboardView_ewalletShowSafe, false);
        array.recycle ();
        initAnim ();
        this.context = context;

        View view = View.inflate (context, R.layout.ewallet_layout_virtual_keyboard, null);
        View layoutSafe = view.findViewById(R.id.layoutSafe);
//        layoutSafe.setVisibility (showSafe ? VISIBLE : GONE);
        layoutSafe.setVisibility ( VISIBLE);
        gridView = view.findViewById (R.id.gv_keybord);
        layoutHide = view.findViewById (R.id.layoutHide);
        imgHide = view.findViewById (R.id.imgHide);
        imgPwd = view.findViewById (R.id.imgPwd);

        initValueList ();

        setupView ();

        imgHide.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                startAnimation (exitAnim);
                setVisibility (View.INVISIBLE);
            }
        });

        imgPwd.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                startAnimation (exitAnim);
                setVisibility (View.INVISIBLE);
            }
        });

        addView (view);
        post (new Runnable () {
            @Override
            public void run() {
                startAnimation (enterAnim);

            }
        });

        gridView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!canEnter) {
                    return;
                }
                if (itemClickListener != null) {
                    itemClickListener.onItemClick (parent, view, position, id);
                }
            }
        });

    }

    public void showOrHide() {
        if (getVisibility () == View.INVISIBLE) {
            show ();
        } else {
            hide ();
        }
    }


    public void show() {
        if (getVisibility () == View.INVISIBLE) {
            setFocusable (true);
            setFocusableInTouchMode (true);
            startAnimation (enterAnim);
            setVisibility (View.VISIBLE);
        }
    }

    public void hide() {
        if (getVisibility () == View.VISIBLE) {
            startAnimation (exitAnim);
            setVisibility (View.INVISIBLE);
        }
    }

    public View getLayoutHide() {
        return layoutHide;
    }

    public ArrayList<String> getValueList() {
        return valueList;
    }

    private void initValueList() {

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                valueList.add (String.valueOf (i));
            } else if (i == 10) {
                if (keyboardType == moneyType) {
                    //钱
                    valueList.add (".");
                } else {
                    if (keyboardType == idCardType) {
                        //身份证
                        valueList.add ("X");
                    } else {
                        valueList.add ("");
                    }
                }
            } else if (i == 11) {
                valueList.add ("0");
            } else if (i == 12) {
                valueList.add ("");
            }
        }
    }

    public GridView getGridView() {
        return gridView;
    }

    private void setupView() {

        EwalletKeyBoardAdapter keyBoardAdapter = new EwalletKeyBoardAdapter (context, valueList);
        if (keyboardType == password || keyboardType==bankCardType) {
            keyBoardAdapter.setEnable (true);
        }
        gridView.setAdapter (keyBoardAdapter);
    }


    public void setKeyboardType(int keyboardType) {
        this.keyboardType = keyboardType;
        valueList.clear ();
        initValueList ();
        setupView ();

    }

    /**
     * 数字键盘显示动画
     */
    private void initAnim() {

        enterAnim = AnimationUtils.loadAnimation (getContext (), R.anim.ewallet_keyborad_push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation (getContext (), R.anim.ewallet_keyborad_push_bottom_out);
    }
}
