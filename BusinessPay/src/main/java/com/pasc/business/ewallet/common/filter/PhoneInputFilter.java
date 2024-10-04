package com.pasc.business.ewallet.common.filter;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public class PhoneInputFilter extends BaseInputFilter {
    int limit=11;
    public PhoneInputFilter(){

    }
    public PhoneInputFilter(int limit) {
        this.limit = limit;
    }
    @Override
    public int limitLength() {
        return limit;
    }

    @Override
    public String regex() {
        return "^[0-9]+$";
    }
}
