package com.pasc.business.ewallet.widget.dialog.common;

import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmChoiceStateListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;

public class ConfirmController {


    private boolean isCancelable;

    private boolean isHideLeftButton;

    private boolean isHideRightButton;

    //设置对话框图片
    private int imageRes;

    private int imageDel;

    //标题
    private CharSequence title;

    private int titleSize;

    private int titleColor;

    private CharSequence desc;

    private int lineSpacingExtra;

    private int descSize;

    private int descColor;

    private boolean enableNoLongerAsked;

    private CharSequence confirmText;

    private  int confirmTextSize;

    private int confirmTextColor;

    private CharSequence closeText;

    private  int closeTextSize;

    private int closeTextColor;
    private AnimationType animationType;

    private OnConfirmListener<ConfirmDialogFragment> onConfirmListener;

    private OnCloseListener<ConfirmDialogFragment> onCloseListener;

    private OnConfirmChoiceStateListener<ConfirmDialogFragment> onConfirmChoiceStateListener;

    public boolean isCancelable() {
        return isCancelable;
    }

    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    public boolean isHideLeftButton() {
        return isHideLeftButton;
    }

    public void setHideLeftButton(boolean hideLeftButton) {
        isHideLeftButton = hideLeftButton;
    }

    public boolean isHideRightButton() {
        return isHideRightButton;
    }

    public void setHideRightButton(boolean hideRightButton) {
        isHideRightButton = hideRightButton;
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

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public CharSequence getDesc() {
        return desc;
    }

    public void setDesc(CharSequence desc) {
        this.desc = desc;
    }

    public int getDescSize() {
        return descSize;
    }

    public void setDescSize(int descSize) {
        this.descSize = descSize;
    }

    public int getDescColor() {
        return descColor;
    }

    public void setDescColor(int descColor) {
        this.descColor = descColor;
    }

    public boolean isEnableNoLongerAsked() {
        return enableNoLongerAsked;
    }

    public void setEnableNoLongerAsked(boolean enableNoLongerAsked) {
        this.enableNoLongerAsked = enableNoLongerAsked;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public CharSequence getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(CharSequence confirmText) {
        this.confirmText = confirmText;
    }

    public int getConfirmTextSize() {
        return confirmTextSize;
    }

    public void setConfirmTextSize(int confirmTextSize) {
        this.confirmTextSize = confirmTextSize;
    }

    public int getConfirmTextColor() {
        return confirmTextColor;
    }

    public void setConfirmTextColor(int confirmTextColor) {
        this.confirmTextColor = confirmTextColor;
    }

    public int getLineSpacingExtra() {
        return lineSpacingExtra;
    }

    public void setLineSpacingExtra(int lineSpacingExtra) {
        this.lineSpacingExtra = lineSpacingExtra;
    }

    public CharSequence getCloseText() {
        return closeText;
    }

    public void setCloseText(CharSequence closeText) {
        this.closeText = closeText;
    }

    public int getCloseTextSize() {
        return closeTextSize;
    }

    public void setCloseTextSize(int closeTextSize) {
        this.closeTextSize = closeTextSize;
    }

    public int getCloseTextColor() {
        return closeTextColor;
    }

    public void setCloseTextColor(int closeTextColor) {
        this.closeTextColor = closeTextColor;
    }

    public OnConfirmListener<ConfirmDialogFragment> getOnConfirmListener() {
        return onConfirmListener;
    }

    public void setOnConfirmListener(OnConfirmListener<ConfirmDialogFragment> onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public OnCloseListener<ConfirmDialogFragment> getOnCloseListener() {
        return onCloseListener;
    }

    public void setOnCloseListener(OnCloseListener<ConfirmDialogFragment> onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public OnConfirmChoiceStateListener<ConfirmDialogFragment> getOnConfirmChoiceStateListener() {
        return onConfirmChoiceStateListener;
    }

    public void setOnConfirmChoiceStateListener(OnConfirmChoiceStateListener<ConfirmDialogFragment> onConfirmChoiceStateListener) {
        this.onConfirmChoiceStateListener = onConfirmChoiceStateListener;
    }

    public static class ControllerParams{

        public boolean isCancelable;

        public boolean isHideLeftButton;

        public boolean isHideRightButton;

        //设置对话框图片
        public int imageRes;

        public int imageDel;

        //标题
        public CharSequence title;

        public int titleSize;

        public int titleColor;

        public CharSequence desc;
        public int lineSpacingExtra;

        public int descSize;

        public int descColor;

        public boolean enableNoLongerAsked;

        public CharSequence confirmText;

        public  int confirmTextSize;

        public int confirmTextColor;

        public CharSequence closeText;

        public  int closeTextSize;

        public int closeTextColor;

        public AnimationType animationType;

        public OnConfirmListener<ConfirmDialogFragment> onConfirmListener;

        public OnCloseListener<ConfirmDialogFragment> onCloseListener;
        public  OnConfirmChoiceStateListener<ConfirmDialogFragment> onConfirmChoiceStateListener;

        public void apply(ConfirmController controller) {

            controller.setCancelable(isCancelable);

            controller.setHideLeftButton(isHideLeftButton);
            controller.setHideRightButton(isHideRightButton);

            controller.setImageRes(imageRes);
            controller.setImageDel(imageDel);
            controller.setTitle(title);
            controller.setTitleSize(titleSize);
            controller.setTitleColor(titleColor);
            controller.setDesc(desc);
            controller.setLineSpacingExtra(lineSpacingExtra);
            controller.setDescSize(descSize);
            controller.setDescColor(descColor);
            controller.setEnableNoLongerAsked(enableNoLongerAsked);
            controller.setConfirmText(confirmText);
            controller.setConfirmTextSize(confirmTextSize);
            controller.setConfirmTextColor(confirmTextColor);
            controller.setCloseText(closeText);
            controller.setCloseTextSize(closeTextSize);
            controller.setCloseTextColor(closeTextColor);
            controller.setAnimationType(animationType);
            controller.setOnConfirmListener(onConfirmListener);
            controller.setOnCloseListener(onCloseListener);
            controller.setOnConfirmChoiceStateListener(onConfirmChoiceStateListener);
        }
    }
}
