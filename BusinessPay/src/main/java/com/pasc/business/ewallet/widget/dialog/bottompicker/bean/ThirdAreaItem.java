package com.pasc.business.ewallet.widget.dialog.bottompicker.bean;


import com.google.gson.annotations.SerializedName;

public class ThirdAreaItem {

    @SerializedName("codeid")
    public String codeid;
    @SerializedName("parentid")
    public String parentid;
    @SerializedName("cityName")
    public String cityName;
    @SerializedName("thirdAreaItem")
    public ThirdAreaItem thirdAreaItem;


    @Override
    public String toString() {
        return cityName;
    }
}
