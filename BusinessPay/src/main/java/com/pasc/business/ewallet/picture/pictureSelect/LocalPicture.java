package com.pasc.business.ewallet.picture.pictureSelect;


import java.io.Serializable;

public class LocalPicture implements Serializable {
    //图片ID
    private String imageId;
    //原图路径
    public String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //是否被选择
    public boolean select = false;


    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public LocalPicture(String path) {
        this.path = path;
    }
    public LocalPicture() {
    }
    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

}
