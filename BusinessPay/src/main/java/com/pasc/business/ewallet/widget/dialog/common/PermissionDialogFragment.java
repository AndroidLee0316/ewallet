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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.widget.DensityUtils;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.dialog.BaseDialogFragment;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;

import java.lang.ref.WeakReference;

public class PermissionDialogFragment extends BaseDialogFragment {
    private static final int DIALOG_DISSMISS = 0;
    private View mView;
    final CommonController controller;
    private RelativeLayout mRelCloseImg;
    public PermissionDialogFragment(){
        controller = new CommonController();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.EwalletInsetDialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

            mView = inflater.inflate(R.layout.ewallet_widget_permission_dialog_fragment,container,false);
            Button confirm = mView.findViewById(R.id.confirm);
            ImageView imgRes= mView.findViewById(R.id.img_id);
            mRelCloseImg = mView.findViewById(R.id.rel_close_img);
            TextView title = mView.findViewById(R.id.title);

            TextView desc = mView.findViewById(R.id.desc);

            ImageView close = mView.findViewById(R.id.close);

            if(!controller.isClosable()){
                mRelCloseImg.setVisibility(View.GONE);
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
            imgRes.setVisibility(View.GONE);
        }else {
            imgRes.setImageResource(controller.getImageRes());
        }
        if(controller.getTitle() != null){
            title.setText(controller.getTitle());

        }else {
            title.setVisibility(View.GONE);
        }
        if(!controller.isTitleVisible()){
            title.setVisibility(View.GONE);
        }

        if(controller.getDesc() !=null ){

            desc.setText(controller.getDesc());

        }else {
            desc.setVisibility(View.GONE);
        }


        if(controller.getConfirmText() != null){
            confirm.setText(controller.getConfirmText());

          }

        final Bundle arguments = getArguments();

        close.setOnClickListener(new View.OnClickListener() {
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
        public final CommonController.ControllerParams mDialogcontroller;

        public Builder(){
            mDialogcontroller = new CommonController.ControllerParams();
        }

        public Builder setCancelable(boolean isCancelable){
            mDialogcontroller.isCancelable = isCancelable;
            return this;
        }

        public Builder setImageRes(@DrawableRes int imageRes) {
            mDialogcontroller.imageRes = imageRes;
            return this;
        }

        public Builder setTitle(CharSequence title){
            mDialogcontroller.title=title;
            return this;
        }

        public Builder setDesc(CharSequence desc) {
            mDialogcontroller.desc = desc;
            return this;
        }

        public Builder setClosable(boolean isClosable) {
            mDialogcontroller.isClosable = isClosable;
            return this;
        }

        public Builder setTitleVisible(boolean isTitleVisible) {
            mDialogcontroller.isTitleVisible = isTitleVisible;
            return this;
        }

        public Builder setConfirmText(CharSequence mConfirmText) {
            mDialogcontroller.confirmText = mConfirmText;
            return this;
        }

        public Builder setOnConfirmListener(OnConfirmListener<PermissionDialogFragment> onConfirmListener) {
            mDialogcontroller.onConfirmListener = onConfirmListener;
            return this;
        }

        public Builder setOnCloseListener(OnCloseListener<PermissionDialogFragment> onCloseListener) {
            mDialogcontroller.onCloseListener = onCloseListener;
            return this;
        }

        public PermissionDialogFragment build(){
            PermissionDialogFragment permissionDialogFragment = new PermissionDialogFragment();
            mDialogcontroller.apply(permissionDialogFragment.controller);
            Bundle args = new Bundle();

            if (mDialogcontroller.onConfirmListener != null) {
                args.putParcelable(ARG_ON_CONFIRM_LISTENER, permissionDialogFragment.obtainMessage(WHAT_ON_CONFIRM_LISTENER, mDialogcontroller.onConfirmListener));
            }
            if (mDialogcontroller.onCloseListener != null) {
                args.putParcelable(ARG_ON_CLOSE_LISTENER, permissionDialogFragment.obtainMessage(WHAT_ON_CLOSE_LISTENER, mDialogcontroller.onCloseListener));
            }
            if (mDialogcontroller.onSingleChoiceListener != null) {
                args.putParcelable(ARG_ON_SINGLE_CHOICE_LISTENER, permissionDialogFragment.obtainMessage(WHAT_ON_SINGLE_CHOICE_LISTENER, mDialogcontroller.onSingleChoiceListener));
            }

            permissionDialogFragment.setArguments(args);

            permissionDialogFragment.setCancelable(mDialogcontroller.isCancelable);
            return permissionDialogFragment;
        }
        public PermissionDialogFragment show(FragmentManager manager, String tag){
            PermissionDialogFragment customDialog = build();
            customDialog.show(manager,tag);
            return customDialog;
        }
    }

    public FragmentManager fragmentManager(){
        FragmentManager fragmentManager = getFragmentManager();
        return fragmentManager;
    }

}
