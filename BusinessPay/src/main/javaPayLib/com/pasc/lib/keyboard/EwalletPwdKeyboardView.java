package com.pasc.lib.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.pasc.business.ewallet.R;
import com.pasc.lib.enc.Epwd;

import java.util.ArrayList;


/**
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify 密码虚拟密码键盘
 **/
public class EwalletPwdKeyboardView extends FrameLayout {
    private int maxLength = 6;
    private Context context;
    private GridView gridView;
    private View layoutHide;
    View imgPwd, imgHide;
    private ArrayList<String> valueList = new ArrayList<> ();
    private Animation enterAnim;
    private Animation exitAnim;
    private boolean canEnter = true;
    private int delayTime = 500;
    public static final int sm3Mode=0;
    public static final int sha256Mode=1;
    public static final int simpleMode=2;
    private Epwd epwd=new Epwd ();
    private int mode=sm3Mode;

    public void setMode(int mode) {
        this.mode = mode;
    }


    public void setFinishDelayTime(int delayTime) {
        this.delayTime = Math.max (delayTime, 0);
    }

    public void setCanEnter(boolean canEnter) {
        this.canEnter = canEnter;
    }

    private void enableSecure(Activity activity) {
        activity.getWindow ().setFlags (WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    private void disableSecure(Activity activity) {
        activity.getWindow ().clearFlags (WindowManager.LayoutParams.FLAG_SECURE);
    }

    public void setMaxLength(int maxLength) {
        if (maxLength > 0) {
            this.maxLength = maxLength;
        }
    }

    public EwalletPwdKeyboardView(Context context) {
        this (context, null);
    }

    public EwalletPwdKeyboardView(Context context, AttributeSet attrs) {
        super (context, attrs);
        this.context = context;
        if (context instanceof Activity) {
            enableSecure ((Activity) context);
        }
        View view = View.inflate (context, R.layout.ewallet_layout_virtual_keyboard, null);
        View layoutSafe = view.findViewById (R.id.layoutSafe);
        layoutSafe.setVisibility (VISIBLE);
        gridView = view.findViewById (R.id.gv_keybord);
        layoutHide = view.findViewById (R.id.layoutHide);
        imgHide = view.findViewById (R.id.imgHide);
        imgPwd = view.findViewById (R.id.imgPwd);
        enterAnim = AnimationUtils.loadAnimation (getContext (), R.anim.ewallet_keyborad_push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation (getContext (), R.anim.ewallet_keyborad_push_bottom_out);
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
                String num = getValueList ().get (position);
                if (position < 11 && position != 9) {    //点击0~9按钮
                    addPassWord (num);
                } else {
                    if (position == 11) {      //点击退格键
                        removePassWord ();
                    }
                }
                if (itemClickListener != null) {
                    itemClickListener.onItemClick (parent, view, position, id);
                }
            }
        });


    }

    private AdapterView.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ArrayList<String> getValueList() {
        return valueList;
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

    private void initValueList() {
        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                valueList.add (String.valueOf (i));
            } else if (i == 10) {
                valueList.add ("");
            } else if (i == 11) {
                valueList.add ("0");
            } else if (i == 12) {
                valueList.add ("");
            }
        }
    }

    private void setupView() {
        EwalletKeyBoardAdapter keyBoardAdapter = new EwalletKeyBoardAdapter (context, valueList);
        keyBoardAdapter.setEnable (true);
        gridView.setAdapter (keyBoardAdapter);
    }

    private EwalletPwdKeyBoardListener listener;

    public void setPwdBoardListener(EwalletPwdKeyBoardListener listener) {
        this.listener = listener;
    }

    private void addPassWord(final String pwd) {
        addPassWordInner (pwd);

    }

    private void removePassWord() {
        epwd.removePassWord ();
        if (listener != null) {
            listener.removePassWord (epwd.currentLen (), maxLength);
        }
    }

    public void clearPassWord() {
        epwd.clearPassWord ();
        if (listener != null) {
            listener.clearPassWord (epwd.currentLen (), maxLength);
        }
    }

    private void addPassWordInner(final String pwd) {
        if (epwd.currentLen  () >= maxLength) {
            return;
        }
        epwd.addPassWord (pwd,mode);
        if (listener != null) {
            listener.addPassWord (epwd.currentLen (), maxLength);
        }

        if (epwd.currentLen ()== maxLength) {
            boolean isSimple = false;
            String nativePwd=epwd.getPwd(mode);
            String pwdArr[]=nativePwd.split ("#split#");
            if (pwdArr.length>=2){
                isSimple= "true".equalsIgnoreCase (pwdArr[1]);
            }
            if (listener != null) {
                listener.onPasswordInputFinish (pwdArr[0], isSimple);
            }
            postDelayed (new Runnable () {
                @Override
                public void run() {
                    clearPassWord ();
                }
            },delayTime);
        }
    }



}
