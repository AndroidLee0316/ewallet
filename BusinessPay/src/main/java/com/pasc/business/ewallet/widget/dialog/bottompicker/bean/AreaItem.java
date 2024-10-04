package com.pasc.business.ewallet.widget.dialog.bottompicker.bean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaItem {

    @SerializedName("codeid")
    public String codeid;
    @SerializedName("parentid")
    public String parentid;
    @SerializedName("cityName")
    public String cityName;
    @SerializedName("children")
    public List<SecondAreaItem> children;

    @Override
    public String toString() {
        return cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<SecondAreaItem> getChildren() {
        return children;
    }

    public void setChildren(List<SecondAreaItem> children) {
        this.children = children;
    }
}
