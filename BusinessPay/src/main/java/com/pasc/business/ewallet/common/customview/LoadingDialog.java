package com.pasc.business.ewallet.common.customview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.common.utils.Util;

import java.lang.reflect.Field;

public class LoadingDialog extends Dialog {
    private final Context context;
    private ImageView img;
    private TextView content;
    private boolean cancelAble=false;
    private View rootView;

    public void setCanCancel(boolean cancelAble){
        this.cancelAble=cancelAble;
        setCancelable(cancelAble);
    }

    public LoadingDialog (@NonNull Context context) {
        super(context ,R.style.EwalletLoadingDialog);
        this.context = context;
        setView();
        setCancelable(cancelAble);

    }

    private void setView () {
        rootView = View.inflate(context, R.layout.ewallet_dialog_loading, null);
        img = rootView.findViewById(R.id.img);
        content = rootView.findViewById(R.id.content);
        setContentView(rootView);
    }


    @Override
    public void show(){
        if (getWindow() != null){
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        super.show();
//        View viewGroup=null;
//        try {
//            Field field= Dialog.class.getDeclaredField ("mDecor");
//            field.setAccessible (true);
//            viewGroup= (View) field.get (this);
//        } catch (Exception e) {
//            e.printStackTrace ();
//        }
//        if (viewGroup!=null){
//            Log.e ("yzj", "show: "+ (viewGroup.getVisibility ()== View.VISIBLE) +" -- "+viewGroup.getClass ().getName ());
//        }
    }

    @Override
    protected void onStart () {
        super.onStart();

        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(-1);
        animation.setInterpolator(new LinearInterpolator ());
        if (img!=null)
            img.startAnimation(animation);
    }

    @Override
    protected void onStop () {
        if (img!=null)
        img.clearAnimation();
        super.onStop();
    }
    /**
     *
     * @param c content;
     */
    public void progress(String c){
        if (content != null){
            if (TextUtils.isEmpty(c)){
                content.setText("");
                content.setVisibility(View.GONE);
            } else{
                content.setText(c);
                content.setVisibility(View.VISIBLE);
            }
        }
    }

}
