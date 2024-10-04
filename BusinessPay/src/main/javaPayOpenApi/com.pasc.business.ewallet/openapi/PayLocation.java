package com.pasc.business.ewallet.openapi;

import com.pasc.business.ewallet.NotProguard;

@NotProguard
public class PayLocation{
    private double longitude ;//经度
    private double latitude ;//纬度

    public PayLocation () {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
