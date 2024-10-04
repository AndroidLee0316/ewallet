package com.pasc.business.ewallet.widget.dialog.bottompicker.bean;

import java.util.List;

/**
 * @date 2019/7/2
 * @des
 * @modify
 **/
public class CityDataBean {
    public List<AreaItem> options1Items;
    public List<List<SecondAreaItem>> options2Items;
    public List<List<List<ThirdAreaItem>>> options3Items;

    public CityDataBean(List<AreaItem> options1Items,
                        List<List<SecondAreaItem>> options2Items,
                        List<List<List<ThirdAreaItem>>> options3Items) {
        this.options1Items = options1Items;
        this.options2Items = options2Items;
        this.options3Items = options3Items;
    }
}
