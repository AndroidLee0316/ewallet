package com.pasc.business.ewallet.widget.dialog.common;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.widget.DensityUtils;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.dialog.BaseDialogFragment;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmChoiceStateListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;

import java.lang.ref.WeakReference;

public class ConfirmDialogFragment extends BaseDialogFragment {
    private static final int DIALOG_DISSMISS = 0;
    private View mView;
    final ConfirmController controller;
    public ConfirmDialogFragment(){
        controller = new ConfirmController();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(controller.getAnimationType() == null){
            setStyle(STYLE_NO_FRAME, R.style.EwalletInsetDialog);
        }else {
            if(controller.getAnimationType() == AnimationType.TRANSLATE_BOTTOM){
                setStyle(STYLE_NO_FRAME, R.style.EwalletSelectItem);
            }else {
                setStyle(STYLE_NO_FRAME, R.style.EwalletInsetDialog);
            }
        }


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

            mView = inflater.inflate(R.layout.ewallet_widget_confirm_dialog,container,false);
            TextView cancel = mView.findViewById(R.id.cancel_tv);
            TextView confirm = mView.findViewById(R.id.confirm_tv);
            ImageView imgres= mView.findViewById(R.id.img_id);

            TextView title = mView.findViewById(R.id.title);

            TextView desc = mView.findViewById(R.id.desc);
            if(controller.getLineSpacingExtra() != 0){
                desc.setLineSpacing(DensityUtils.dp2px(controller.getLineSpacingExtra()),
                        1);
            }

            RelativeLayout relDelImg = mView.findViewById(R.id.rel_del_img);
            ImageView imgDel = mView.findViewById(R.id.close);

           LinearLayout linCheck = mView.findViewById(R.id.lin_check);
            LinearLayout ll_btn = mView.findViewById(R.id.ll_btn);
            View mLineView = mView.findViewById(R.id.line_view);
            View mverticalLine = mView.findViewById(R.id.vertical_line);
//            TextView confirmSingleTv = mView.findViewById(R.id.confirm_single_tv);

            final CheckBox checkBox = mView.findViewById(R.id.check_id);

            if(controller.getImageDel() != 0){
                imgDel.setImageResource(controller.getImageDel());
                relDelImg.setVisibility(View.VISIBLE);
             }else {
                relDelImg.setVisibility(View.GONE);
            }
            if(controller.getImageRes() != 0){
                if(controller.getTitle() != null && controller.getDesc() !=null ){

                    LinearLayout.LayoutParams layoutTitle=(LinearLayout.LayoutParams)title.getLayoutParams();
                    setMargins(layoutTitle,11);
                    desc.setLayoutParams(layoutTitle);
                    LinearLayout.LayoutParams layoutDesc=(LinearLayout.LayoutParams)desc.getLayoutParams();
                    setMargins(layoutDesc,4);
                    desc.setLayoutParams(layoutDesc);


                }
                if(controller.getTitle() != null && controller.getDesc() ==null ){

                    LinearLayout.LayoutParams layoutTitle=(LinearLayout.LayoutParams)title.getLayoutParams();
                    setMargins(layoutTitle,11);
                    desc.setLayoutParams(layoutTitle);


                }
                if(controller.getTitle() == null && controller.getDesc() !=null ){

                    LinearLayout.LayoutParams layoutDesc=(LinearLayout.LayoutParams)desc.getLayoutParams();
                    setMargins(layoutDesc,11);
                    desc.setLayoutParams(layoutDesc);


                }

            }else {
                if(controller.getTitle() != null && controller.getDesc() !=null ){
                 LinearLayout.LayoutParams layoutDesc=(LinearLayout.LayoutParams)desc.getLayoutParams();
                setMargins(layoutDesc,8);
                desc.setLayoutParams(layoutDesc);


                }

            }

        if(controller.getImageRes() == 0){
            imgres.setVisibility(View.GONE);
        }else {
            imgres.setImageResource(controller.getImageRes());
        }
        if(controller.getTitle() != null){
            title.setText(controller.getTitle());
            if(controller.getTitleSize() != 0){
                title.setTextSize(controller.getTitleSize());
            }
            if(controller.getTitleColor() != 0){
                title.setTextColor(controller.getTitleColor());
            }
        }else {
            title.setVisibility(View.GONE);
        }

        if(controller.getDesc() !=null ){

            desc.setText(controller.getDesc());
            if(controller.getDescSize() != 0){
                desc.setTextSize(controller.getDescSize());
            }
            if(controller.getDescColor() != 0){
                desc.setTextColor(controller.getDescColor());
            }
        }else {
            desc.setVisibility(View.GONE);
        }
        if(controller.isEnableNoLongerAsked()){
            linCheck.setVisibility(View.VISIBLE);
        }else {
            linCheck.setVisibility(View.GONE);

        }

        if(controller.isHideLeftButton()){
            mverticalLine.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }

        if(controller.isHideRightButton()){
            mverticalLine.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
        }

        if(controller.getConfirmText() != null){
            confirm.setText(controller.getConfirmText());

          }
        if(controller.getConfirmTextSize() != 0){

            confirm.setTextSize(controller.getConfirmTextSize());
        }
        if(controller.getConfirmTextColor() != 0){

            confirm.setTextColor(controller.getConfirmTextColor());
        }
        if(controller.getCloseText() != null){
            cancel.setText(controller.getCloseText());

        }
        if(controller.getCloseTextSize() != 0){

            cancel.setTextSize(controller.getCloseTextSize());
        }
        if(controller.getCloseTextColor() != 0){

            cancel.setTextColor(controller.getCloseTextColor());
        }
        final Bundle arguments = getArguments();

        linCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                }else {
                    checkBox.setChecked(true);
                }

            }
        });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean hasListener = false;
                    if (arguments != null) {
                        Parcelable parcelable = arguments.getParcelable(ARG_ON_CLOSE_LISTENER);
                        if (parcelable != null && parcelable instanceof Message) {
                            Message.obtain(((Message) parcelable)).sendToTarget();
                            hasListener = true;
                        }
                    }
                    if (!hasListener) {
                        // 如果没有设置监听，则关闭窗口
                        dismiss();
                    }

                }
            });
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasListener = false;
                if (arguments != null) {
                    Parcelable parcelable = arguments.getParcelable(ARG_ON_CLOSE_LISTENER);
                    if (parcelable != null && parcelable instanceof Message) {
                        Message.obtain(((Message) parcelable)).sendToTarget();
                        hasListener = true;
                    }
                }
                if (!hasListener) {
                    // 如果没有设置监听，则关闭窗口
                    dismiss();
                }
            }
        });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   if(controller.isEnableNoLongerAsked()){
                       boolean isCheckState = checkBox.isChecked();

                       int postion = 0;
                       if(isCheckState){
                           postion = 1;
                       }
                       sendMessageConfirmChoiceState(postion);
                   }else {
                       boolean hasListener = false;
                       if (arguments != null) {
                           Parcelable parcelable = arguments.getParcelable(ARG_ON_CONFIRM_LISTENER);
                           if (parcelable != null && parcelable instanceof Message) {
                               Message.obtain(((Message) parcelable)).sendToTarget();
                               hasListener = true;
                           }
                       }
                       if (!hasListener) {
                           // 如果没有设置监听，则关闭窗口
                           dismiss();
                       }
                   }


                }
            });

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return mView;
    }


    private void setMargins(LinearLayout.LayoutParams layoutParams,int marginsTopDp){
        layoutParams.setMargins(0, DensityUtils.dp2px(marginsTopDp),0,0);
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(controller.isCancelable());

    }

    public interface OnBindViewListener{
        void getBindView(View view);
    }

    @Override
    public void onPause() {
        super.onPause();
//        mHandler.removeCallbacksAndMessages(null);
    }

    public static class MyHandler extends Handler {
        private WeakReference<Dialog> mDialog;
        public MyHandler(Dialog dialog){
            mDialog = new WeakReference<Dialog>(dialog);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DIALOG_DISSMISS:
                    mDialog.get().dismiss();
            }
        }
    }


    public static class Builder{
        public final ConfirmController.ControllerParams mDialogcontroller;

        public Builder(){
            mDialogcontroller = new ConfirmController.ControllerParams();
        }

        public Builder setCancelable(boolean isCancelable){
            mDialogcontroller.isCancelable = isCancelable;
            return this;
        }
        public Builder setHideCloseButton(boolean isHide){
            mDialogcontroller.isHideLeftButton = isHide;
            return this;
        }
        public Builder setHideConfirmButton(boolean isHide){
            mDialogcontroller.isHideRightButton = isHide;
            return this;
        }


        public Builder setImageRes(@DrawableRes int imageRes) {
            mDialogcontroller.imageRes = imageRes;
            return this;
        }

        public Builder setImageDel(@DrawableRes int imageRes) {
            mDialogcontroller.imageDel = imageRes;
            return this;
        }

        public Builder setTitle(CharSequence title){
            mDialogcontroller.title=title;
            return this;
        }
        public Builder setTitleSize(int size){
            mDialogcontroller.titleSize=size;
            return this;
        }
        public Builder setTitleColor(int color){
            mDialogcontroller.titleColor=color;
            return this;
        }
        public Builder setDesc(CharSequence desc) {
            mDialogcontroller.desc = desc;
            return this;
        }

        public Builder setDescLineSpacingExtra(int spacing) {
            mDialogcontroller.lineSpacingExtra = spacing;
            return this;
        }

        public Builder setDescSize(int descSize) {
            mDialogcontroller.descSize = descSize;
            return this;
        }

        public Builder setDescColor(int descColor) {
            mDialogcontroller.descColor = descColor;
            return this;
        }

        public Builder isEnableNoLongerAsked(boolean isEnable) {
            mDialogcontroller.enableNoLongerAsked = isEnable;
            return this;
        }


        public Builder setConfirmText(CharSequence mConfirmText) {
            mDialogcontroller.confirmText = mConfirmText;
            return this;
        }
        public Builder setConfirmTextSize(int mConfirmTextSize) {
            mDialogcontroller.confirmTextSize = mConfirmTextSize;
            return this;
        }
        public Builder setConfirmTextColor(int mConfirmTextColor) {
            mDialogcontroller.confirmTextColor = mConfirmTextColor;
            return this;
        }

        public Builder setCloseText(CharSequence mCloseText) {
            mDialogcontroller.closeText = mCloseText;
            return this;
        }
        public Builder setCloseTextSize(int negativeTextSize) {
            mDialogcontroller.closeTextSize = negativeTextSize;
            return this;
        }
        public Builder setCloseTextColor(int negativeTextColor) {
            mDialogcontroller.closeTextColor = negativeTextColor;
            return this;
        }
        public Builder setAnimationType(AnimationType animationType) {
            mDialogcontroller.animationType = animationType;
            return this;
        }

        public Builder setOnConfirmListener(OnConfirmListener<ConfirmDialogFragment> onConfirmListener) {
            mDialogcontroller.onConfirmListener = onConfirmListener;
            return this;
        }
        public Builder setOnConfirmChoiceStateListener(OnConfirmChoiceStateListener<ConfirmDialogFragment> onConfirmChoiceStateListener) {
            mDialogcontroller.onConfirmChoiceStateListener = onConfirmChoiceStateListener;
            return this;
        }

        public Builder setOnCloseListener(OnCloseListener<ConfirmDialogFragment> onCloseListener) {
            mDialogcontroller.onCloseListener = onCloseListener;
            return this;
        }

        public ConfirmDialogFragment build(){
            ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
            mDialogcontroller.apply(confirmDialogFragment.controller);
            Bundle args = new Bundle();
//            if ( mDialogcontroller.onDismissListener != null) {
//
//                args.putParcelable(ARG_ON_DISMISS_LISTENER, confirmDialogFragment.obtainMessage(WHAT_ON_DISMISS_LISTENER, mDialogcontroller.onDismissListener));
//            }
//            confirmDialogFragment.setArguments(args);
//            if ( mDialogcontroller.onConfirmPositiveListener != null) {
//                args.putParcelable(ARG_ON_CONFIRM_LISTENER, confirmDialogFragment.obtainMessage(WHAT_ON_CONFIRM_LISTENER, mDialogcontroller.onConfirmPositiveListener));
//            }
//            confirmDialogFragment.setArguments(args);

            if (mDialogcontroller.onConfirmListener != null) {
                args.putParcelable(ARG_ON_CONFIRM_LISTENER, confirmDialogFragment.obtainMessage(WHAT_ON_CONFIRM_LISTENER, mDialogcontroller.onConfirmListener));
            }
            if (mDialogcontroller.onCloseListener != null) {
                args.putParcelable(ARG_ON_CLOSE_LISTENER, confirmDialogFragment.obtainMessage(WHAT_ON_CLOSE_LISTENER, mDialogcontroller.onCloseListener));
            }

            if (mDialogcontroller.onConfirmChoiceStateListener != null) {
                args.putParcelable(ARG_ON_CONFIRM_CHOICE_STATE_LISTENER, confirmDialogFragment.obtainMessage(WHAT_ON_CONFIRM_CHOICE_STATE_LISTENER, mDialogcontroller.onConfirmChoiceStateListener));
            }

            confirmDialogFragment.setArguments(args);

            confirmDialogFragment.setCancelable(mDialogcontroller.isCancelable);
            return confirmDialogFragment;
        }
        public ConfirmDialogFragment show(FragmentManager manager, String tag){
            ConfirmDialogFragment customDialog = build();
            customDialog.show(manager,tag);
            return customDialog;
        }
    }

    public FragmentManager fragmentManager(){
        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager;
    }

}
