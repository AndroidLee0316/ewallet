package com.pasc.business.ewallet.business.home.bean;

/**
 * @date 2019/8/14
 * @des
 * @modify
 **/
public class HomeItemBean {
    public static final int RECHARGE = 0;
    public static final int WITHDRAW = 1;
    public static final int BANKCARD = 2;

    private int type = RECHARGE;

    private int icon;

    private String text;

    private int bgIcon;

    private int bg = -1;


    public int getBg() {
        return bg;
    }

    public int getBgIcon() {
        return bgIcon;
    }

    public int getIcon() {
        return icon;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public HomeItemBean type(int type) {
        this.type = type;
        return this;
    }

    public HomeItemBean icon(int icon) {
        this.icon = icon;
        return this;
    }

    public HomeItemBean text(String text) {
        this.text = text;
        return this;
    }

    public HomeItemBean bgIcon(int bgIcon) {
        this.bgIcon = bgIcon;
        return this;
    }

    public HomeItemBean bg(int bg) {
        this.bg = bg;
        return this;
    }
}
