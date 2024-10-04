package com.pasc.business.ewallet.widget.dialog.bottompicker;

import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.bottompicker.utils.PickerDateType;

import java.util.List;

public class pickerController<T> {


    public CharSequence closeText;
    //标题
    private CharSequence title;

    private CharSequence confirmText;

    private PickerDateType pickerDateType;

    private boolean circling;

    private boolean isList;

    private  String[] items;

    private int currentPosition;

    private int startYear;
    private int endYear;
    int startMonth=-1;
    int endMonth=-1;

    private List<T> options1Items;
    private List<List<T>> options2Items;
    private List<List<List<T>>> options3Items;

    private int option1;
    private int option2;
    private int option3;

    public List<T> getOptions1Items() {
        return options1Items;
    }

    public void setOptions1Items(List<T> options1Items) {
        this.options1Items = options1Items;
    }

    public List<List<T>> getOptions2Items() {
        return options2Items;
    }

    public void setOptions2Items(List<List<T>> options2Items) {
        this.options2Items = options2Items;
    }

    public List<List<List<T>>> getOptions3Items() {
        return options3Items;
    }

    public void setOptions3Items(List<List<List<T>>> options3Items) {
        this.options3Items = options3Items;
    }

    private OnConfirmListener<DatePickerDialogFragment> onConfirmListener;

    private OnCloseListener<DatePickerDialogFragment> onCloseListener;


    private OnConfirmListener<ListPickerDialogFragment> onListConfirmListener;

    private OnCloseListener<ListPickerDialogFragment> onListCloseListener;

    private OnConfirmListener<CityPickerDialogFragment> onCityConfirmListener;

    private OnCloseListener<CityPickerDialogFragment> onCityCloseListener;


    public CharSequence getCloseText() {
        return closeText;
    }

    public void setCloseText(CharSequence closeText) {
        this.closeText = closeText;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(CharSequence confirmText) {
        this.confirmText = confirmText;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public PickerDateType getPickerDateType() {
        return pickerDateType;
    }

    public void setPickerDateType(PickerDateType pickerDateType) {
        this.pickerDateType = pickerDateType;
    }

    public int getOption1() {
        return option1;
    }

    public void setOption1(int option1) {
        this.option1 = option1;
    }

    public int getOption2() {
        return option2;
    }

    public void setOption2(int option2) {
        this.option2 = option2;
    }

    public int getOption3() {
        return option3;
    }

    public void setOption3(int option3) {
        this.option3 = option3;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isCircling() {
        return circling;
    }

    public void setCircling(boolean circling) {
        this.circling = circling;
    }

    public OnConfirmListener<DatePickerDialogFragment> getOnConfirmListener() {
        return onConfirmListener;
    }

    public void setOnConfirmListener(OnConfirmListener<DatePickerDialogFragment> onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public OnCloseListener<DatePickerDialogFragment> getOnCloseListener() {
        return onCloseListener;
    }

    public void setOnCloseListener(OnCloseListener<DatePickerDialogFragment> onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public OnConfirmListener<ListPickerDialogFragment> getOnListConfirmListener() {
        return onListConfirmListener;
    }

    public void setOnListConfirmListener(OnConfirmListener<ListPickerDialogFragment> onListConfirmListener) {
        this.onListConfirmListener = onListConfirmListener;
    }

    public OnCloseListener<ListPickerDialogFragment> getOnListCloseListener() {
        return onListCloseListener;
    }

    public void setOnListCloseListener(OnCloseListener<ListPickerDialogFragment> onListCloseListener) {
        this.onListCloseListener = onListCloseListener;
    }

    public OnConfirmListener<CityPickerDialogFragment> getOnCityConfirmListener() {
        return onCityConfirmListener;
    }

    public void setOnCityConfirmListener(OnConfirmListener<CityPickerDialogFragment> onCityConfirmListener) {
        this.onCityConfirmListener = onCityConfirmListener;
    }

    public OnCloseListener<CityPickerDialogFragment> getOnCityCloseListener() {
        return onCityCloseListener;
    }

    public void setOnCityCloseListener(OnCloseListener<CityPickerDialogFragment> onCityCloseListener) {
        this.onCityCloseListener = onCityCloseListener;
    }


    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public static class ControllerParams<T>{

        public CharSequence closeText;

        public CharSequence title;

        public CharSequence confirmText;

        public PickerDateType pickerDateType;
        public boolean circling;
        public boolean isList;
        public String[] items;



        public  int currentPosition;

        public int startYear;
        public int endYear;

        public int startMonth=0;
        public int endMonth=11;
        public List<T> options1Items;
        public List<List<T>> options2Items;
        public List<List<List<T>>> options3Items;
        public int option1;
        public int option2;
        public int option3;

        public OnConfirmListener<DatePickerDialogFragment> onConfirmListener;

        public OnCloseListener<DatePickerDialogFragment> onCloseListener;

        public OnConfirmListener<ListPickerDialogFragment> onListConfirmListener;

        public OnCloseListener<ListPickerDialogFragment> onListCloseListener;

        public OnConfirmListener<CityPickerDialogFragment> onCityConfirmListener;

        public OnCloseListener<CityPickerDialogFragment> onCityCloseListener;

        public void apply(pickerController controller) {

            controller.setCloseText(closeText);
            controller.setTitle(title);
            controller.setConfirmText(confirmText);
            controller.setCircling(circling);
            controller.setList(isList);
            controller.setPickerDateType(pickerDateType);

            controller.setItems(items);
            controller.setCurrentPosition(currentPosition);
            controller.setOnCloseListener(onCloseListener);
            controller.setOnConfirmListener(onConfirmListener);

            controller.setOnListCloseListener(onListCloseListener);
            controller.setOnListConfirmListener(onListConfirmListener);

            controller.setOnCityConfirmListener(onCityConfirmListener);
            controller.setOnCityCloseListener(onCityCloseListener);

            controller.setStartYear(startYear);
            controller.setEndYear(endYear);

            controller.setStartMonth (startMonth);
            controller.setEndMonth (endMonth);

            controller.setOptions1Items(options1Items);
            controller.setOptions2Items(options2Items);
            controller.setOptions3Items(options3Items);
            controller.setOption1(option1);
            controller.setOption2(option2);
            controller.setOption3(option3);

        }
    }
}
