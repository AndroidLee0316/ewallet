package com.pasc.business.ewallet.common.filter;

/**
 * @date 2019/7/15
 * @des
 * @modify
 **/
public class ChineseInputFilter extends BaseInputFilter {
    int limit=18;
    public ChineseInputFilter(){

    }
    public ChineseInputFilter(int limit) {
        this.limit = limit;
    }

    @Override
    public int limitLength() {
        return limit;
    }

    @Override
    public String regex() {
        return "[\\u4e00-\\u9fa5\\.]+";
    }
}
