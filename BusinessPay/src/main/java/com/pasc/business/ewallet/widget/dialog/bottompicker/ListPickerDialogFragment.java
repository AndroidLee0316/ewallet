package com.pasc.business.ewallet.widget.dialog.bottompicker;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.widget.dialog.BaseDialogFragment;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.bottompicker.widget.DatePicker;

public class ListPickerDialogFragment extends BaseDialogFragment {

    final pickerController  controller;

    public void setPosition (int position) {
        mPosition = position;
    }

    private int mPosition;

    public ListPickerDialogFragment(){
        controller = new pickerController();
    }

    public int getPosition(){
        return  mPosition;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_NoTitle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(initView());
        return dialog;
    }

    View initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.ewallet_widget_date_picker_dialog, null);
        TextView cancelTv = view.findViewById(R.id.close_tv);
        TextView titleTv = view.findViewById(R.id.title_tv);
        TextView confirmTv = view.findViewById(R.id.confirm_tv);

        if(controller.getCloseText() != null){
            cancelTv.setText(controller.getCloseText());
        }
        if(controller.getTitle() != null){
            titleTv.setText(controller.getTitle());
            titleTv.setVisibility (View.VISIBLE);
        }

        if(controller.getConfirmText() != null){
            confirmTv.setText(controller.getConfirmText());
        }

        DatePicker datePicker = view.findViewById(R.id.date_picker);

            if(controller.getItems() != null){
                if(controller.getItems().length>0){
                    datePicker.showList(controller.getItems(), mPosition, controller.isCircling());
                }
            }

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }

            @Override
            public void onItemChanged(int position) {

                mPosition = position;

            }
        });
        final Bundle arguments = getArguments();

        cancelTv.setOnClickListener(new View.OnClickListener() {
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

        confirmTv.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    public static class Builder{
        public final pickerController.ControllerParams mDialogcontroller;
        public Builder(){
            mDialogcontroller = new pickerController.ControllerParams();
        }

        public Builder setCloseText(CharSequence mCloseText) {
            mDialogcontroller.closeText = mCloseText;
            return this;
        }

        public Builder setTitle(CharSequence title){
            mDialogcontroller.title=title;
            return this;
        }

        public Builder setConfirmText(CharSequence mConfirmText) {
            mDialogcontroller.confirmText = mConfirmText;
            return this;
        }

        public Builder setItems(String[] items, int mCurrentPostion){
            mDialogcontroller.items = items;
            mDialogcontroller.currentPosition = mCurrentPostion;
            mDialogcontroller.isList = true;
            return this;
        }

        public Builder setOnConfirmListener(OnConfirmListener<ListPickerDialogFragment> onConfirmListener) {
            mDialogcontroller.onListConfirmListener = onConfirmListener;
            return this;
        }
        public Builder setCircling(boolean isCircling) {
            mDialogcontroller.circling = isCircling;
            return this;
        }



        public Builder setOnCloseListener(OnCloseListener<ListPickerDialogFragment> onCloseListener) {
            mDialogcontroller.onListCloseListener = onCloseListener;
            return this;
        }


        public ListPickerDialogFragment build(){
            ListPickerDialogFragment listPickerDialogFragment = new ListPickerDialogFragment();
            mDialogcontroller.apply(listPickerDialogFragment.controller);
            listPickerDialogFragment.mPosition = mDialogcontroller.currentPosition;

            Bundle args = new Bundle();

            if (mDialogcontroller.onListConfirmListener != null) {
                args.putParcelable(ARG_ON_CONFIRM_LISTENER, listPickerDialogFragment.obtainMessage(WHAT_ON_CONFIRM_LISTENER, mDialogcontroller.onListConfirmListener));
            }
            if (mDialogcontroller.onListCloseListener != null) {
                args.putParcelable(ARG_ON_CLOSE_LISTENER, listPickerDialogFragment.obtainMessage(WHAT_ON_CLOSE_LISTENER, mDialogcontroller.onListCloseListener));
            }
            listPickerDialogFragment.setArguments(args);


            return listPickerDialogFragment;
        }
        public ListPickerDialogFragment show(FragmentManager manager, String tag){
            ListPickerDialogFragment customDialog = build();
            customDialog.show(manager,tag);
            return customDialog;
        }

    }



}
