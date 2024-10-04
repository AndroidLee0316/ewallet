package com.pasc.business.ewallet.picture.pictureSelect;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;

public class ImagePicker implements Serializable {

    public static final String TAG = ImagePicker.class.getSimpleName();
    private boolean multiMode = true;    //图片选择模式
    private static int selectLimit =-1;         //最大选择图片数量
    private boolean enableCompress;


    private boolean showCamera = false;   //显示相机

    private ImagePicker() {
    }

    private static class SingletonHolder {
        private static final ImagePicker INSTANCE = new ImagePicker();
    }

    public static  ImagePicker getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static boolean isEnable() {
        return getSelectLimit()>0;
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    public ImagePicker setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
        return this;
    }

    public static int getSelectLimit() {
        return selectLimit;
    }

    public ImagePicker setSelectLimit(int limit) {
        selectLimit = limit;
        return this;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public ImagePicker setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return this;
    }

    public ImagePicker setEnableCompress(boolean enableCompress) {
        this.enableCompress = enableCompress;
        return this;
    }

    /**
     * 选择多张照片的方法
     * @param context
     * @param requestCode
     */
    public void pickMutlPhoto(Activity context, int requestCode) {
        Intent intent = new Intent (context, NewPictureSelectActivity.class);
        NewPictureSelectActivity.setIsHeadImg(false);
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * 选择头像的方法
     * @param context
     * @param requestCode
     */
    public void pickHeadPhoto(Activity context, int requestCode) {
        Intent intent = new Intent (context, NewPictureSelectActivity.class);
        NewPictureSelectActivity.setIsHeadImg(true);
        context.startActivityForResult(intent, requestCode);
    }


    public boolean isEnableCompress() {
        return enableCompress;
    }
}