package com.pasc.business.ewallet.widget.dialog.common;

import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.OnSingleChoiceListener;

import java.util.ArrayList;

public class CommonController {

    private boolean isCancelable;

    //设置对话框图片
    private int imageRes;
    private int imageDel;
    //标题
    private CharSequence title;
    private CharSequence desc;

    private CharSequence confirmText;

    private CharSequence closeText;

    private boolean isClosable;
    private boolean isTitleVisible;


    private ArrayList<CharSequence> items;

    private OnConfirmListener<PermissionDialogFragment> onConfirmListener;

    private OnCloseListener<PermissionDialogFragment> onCloseListener;

    private OnSingleChoiceListener<PermissionDialogFragment> onSingleChoiceListener; // 单选监听器



    public boolean isCancelable() {
        return isCancelable;
    }

    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    public boolean isClosable() {
        return isClosable;
    }

    public void setClosable(boolean closable) {
        isClosable = closable;
    }

    public boolean isTitleVisible() {
        return isTitleVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        isTitleVisible = titleVisible;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getImageDel() {
        return imageDel;
    }

    public void setImageDel(int imageDel) {
        this.imageDel = imageDel;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getDesc() {
        return desc;
    }

    public void setDesc(CharSequence desc) {
        this.desc = desc;
    }



    public CharSequence getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(CharSequence confirmText) {
        this.confirmText = confirmText;
    }


    public ArrayList<CharSequence> getItems() {
        return items;
    }

    public void setItems(ArrayList<CharSequence> items) {
        this.items = items;
    }

    public CharSequence getCloseText() {
        return closeText;
    }

    public void setCloseText(CharSequence closeText) {
        this.closeText = closeText;
    }

    public OnConfirmListener<PermissionDialogFragment> getOnConfirmListener() {
        return onConfirmListener;
    }

    public void setOnConfirmListener(OnConfirmListener<PermissionDialogFragment> onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public OnCloseListener<PermissionDialogFragment> getOnCloseListener() {
        return onCloseListener;
    }

    public void setOnCloseListener(OnCloseListener<PermissionDialogFragment> onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public OnSingleChoiceListener<PermissionDialogFragment> getOnSingleChoiceListener() {
        return onSingleChoiceListener;
    }

    public void setOnSingleChoiceListener(OnSingleChoiceListener<PermissionDialogFragment> onSingleChoiceListener) {
        this.onSingleChoiceListener = onSingleChoiceListener;
    }

    public static class ControllerParams{

        public boolean isCancelable;
        //设置对话框图片
        public int imageRes;

        public int imageDel;
        //标题
        public CharSequence title;

        public CharSequence desc;

        public CharSequence confirmText;

        public CharSequence closeText;

        public boolean isClosable = true;
        public boolean isTitleVisible = true;

        public ArrayList<CharSequence> items;

        public OnConfirmListener<PermissionDialogFragment> onConfirmListener;

        public OnCloseListener<PermissionDialogFragment> onCloseListener;

        public OnSingleChoiceListener<PermissionDialogFragment> onSingleChoiceListener; // 单选监听器

        public void apply(CommonController controller) {

            controller.setCancelable(isCancelable);

            controller.setImageRes(imageRes);
            controller.setImageDel(imageDel);
            controller.setTitle(title);
            controller.setDesc(desc);
            controller.setClosable(isClosable);
            controller.setTitleVisible(isTitleVisible);
            controller.setConfirmText(confirmText);
            controller.setCloseText(closeText);
            controller.setItems(items);
            controller.setOnConfirmListener(onConfirmListener);
            controller.setOnCloseListener(onCloseListener);
            controller.setOnSingleChoiceListener(onSingleChoiceListener);
        }
    }
}
