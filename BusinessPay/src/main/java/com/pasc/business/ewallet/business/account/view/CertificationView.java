package com.pasc.business.ewallet.business.account.view;

import com.pasc.business.ewallet.base.CommonBaseView;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.CityDataBean;

/**
 * @date 2019/7/3
 * @des
 * @modify
 **/
public interface CertificationView extends CommonBaseView {
    void loadCityDataSuccess(CityDataBean cityDataBean);
    void loadCityDataError(String code,String msg);
    void loadOccupationSuccess(boolean isInit,String[] occupationNames,String[] occupationIds);
    void loadOccupationError(String code,String msg);


}
