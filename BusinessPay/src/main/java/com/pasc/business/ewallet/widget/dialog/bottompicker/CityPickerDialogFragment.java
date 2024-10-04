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
import com.pasc.business.ewallet.widget.dialog.bottompicker.widget.CityPicker;

import java.util.List;

public class CityPickerDialogFragment<T> extends BaseDialogFragment {

    final pickerController  controller;
    private int options1;
    private int options2;
    private int options3;

    private List<T> options1Items;
    private List<List<T>> options2Items;
    private List<List<List<T>>> options3Items;


    public interface OnOptionsSelectListener {
        void onOptionsSelect(int var1, int var2, int var3);
    }

    public void setOnOptionsSelectListener(OnOptionsSelectListener listener) {
        OnOptionsSelectListener onOptionsSelectListener = listener;
    }

    public CityPickerDialogFragment(){
        controller = new pickerController();
    }


    public int getOptions1() {
        return options1;
    }

    public void setOptions1(int options1) {
        this.options1 = options1;
    }

    public int getOptions2() {
        return options2;
    }

    public void setOptions2(int options2) {
        this.options2 = options2;
    }

    public int getOptions3() {
        return options3;
    }

    public void setOptions3(int options3) {
        this.options3 = options3;
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
        View view = inflater.inflate(R.layout.ewallet_widget_city_picker_dialog, null);
        TextView cancelTv = view.findViewById(R.id.close_tv);
        TextView titleTv = view.findViewById(R.id.title_tv);
        TextView confirmTv = view.findViewById(R.id.confirm_tv);

        if(controller.getCloseText() != null){
            cancelTv.setText(controller.getCloseText());
        }
        if(controller.getTitle() != null){
            titleTv.setText(controller.getTitle());
        }

        if(controller.getConfirmText() != null){
            confirmTv.setText(controller.getConfirmText());
        }

        CityPicker cityPicker = view.findViewById(R.id.city_picker);

            if(controller.getItems() != null){
                if(controller.getItems().length>0){
                    options1 = controller.getCurrentPosition();
                    //cityPicker.showList(controller.getItems(),controller.getCurrentPosition());
                }
            }
         cityPicker.setCircle(controller.isCircling());
         cityPicker.setPicker(controller.getOptions1Items(),controller.getOptions2Items(),controller.getOptions3Items());

         cityPicker.setSelectOptions(options1, options2, options3);

         cityPicker.setOnDateChangedListener(new CityPicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(int mOptions1, int mOptions2, int mOptions3) {
                options1 = mOptions1;
                options2 = mOptions2;
                options3 = mOptions3;
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

    public static class Builder<T>{
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

        public Builder setPicker(List<T> mOptions1Items, List<List<T>> mOptions2Items, List<List<List<T>>> mOptions3Items) {
            mDialogcontroller.options1Items =mOptions1Items;
            mDialogcontroller.options2Items =mOptions2Items;
            mDialogcontroller.options3Items =mOptions3Items;

            return this;
        }

        public Builder setSelectOptions(int option1, int option2, int option3) {
            mDialogcontroller.option1 = option1;
            mDialogcontroller.option2 = option2;
            mDialogcontroller.option3 = option3;
            return this;
        }

        public Builder setItems(String[] items, int mCurrentPostion){
            mDialogcontroller.items = items;
            mDialogcontroller.currentPosition = mCurrentPostion;
            mDialogcontroller.isList = true;
            return this;
        }

        public Builder setOnConfirmListener(OnConfirmListener<CityPickerDialogFragment> onConfirmListener) {
            mDialogcontroller.onCityConfirmListener = onConfirmListener;
            return this;
        }
        public Builder setCircling(boolean isCircling) {
            mDialogcontroller.circling = isCircling;
            return this;
        }


        public Builder setOnCloseListener(OnCloseListener<CityPickerDialogFragment> onCloseListener) {
            mDialogcontroller.onCityCloseListener = onCloseListener;
            return this;
        }


        public CityPickerDialogFragment build(){
            CityPickerDialogFragment cityPickerDialogFragment = new CityPickerDialogFragment();
            mDialogcontroller.apply(cityPickerDialogFragment.controller);
            cityPickerDialogFragment.options1 = mDialogcontroller.option1;
            cityPickerDialogFragment.options2 = mDialogcontroller.option2;
            cityPickerDialogFragment.options3 = mDialogcontroller.option3;

            Bundle args = new Bundle();

            if (mDialogcontroller.onCityConfirmListener != null) {
                args.putParcelable(ARG_ON_CONFIRM_LISTENER, cityPickerDialogFragment.obtainMessage(WHAT_ON_CONFIRM_LISTENER, mDialogcontroller.onCityConfirmListener));
            }
            if (mDialogcontroller.onCityCloseListener != null) {
                args.putParcelable(ARG_ON_CLOSE_LISTENER, cityPickerDialogFragment.obtainMessage(WHAT_ON_CLOSE_LISTENER, mDialogcontroller.onCityCloseListener));
            }
            cityPickerDialogFragment.setArguments(args);


            return cityPickerDialogFragment;
        }
        public CityPickerDialogFragment show(FragmentManager manager, String tag){
            CityPickerDialogFragment customDialog = build();
            customDialog.show(manager,tag);
            return customDialog;
        }

    }



}
