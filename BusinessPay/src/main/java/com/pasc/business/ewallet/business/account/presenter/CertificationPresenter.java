package com.pasc.business.ewallet.business.account.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pasc.business.ewallet.base.EwalletBasePresenter;
import com.pasc.business.ewallet.business.account.view.CertificationView;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.AreaItem;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.CityDataBean;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.SecondAreaItem;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.ThirdAreaItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @date 2019/2/19
 */
public class CertificationPresenter extends EwalletBasePresenter<CertificationView> {
    private CompositeDisposable compositeDisposable = new CompositeDisposable ();
    private String[] occupationNames = {
            "办事人员及企业员工",
            "其他社会生产人员",
            "批发与零售服务人员",
            "软件和信息技术服务人员",
            "住宿和餐饮服务人员",
            "金融服务人员",
            "房地产服务人员",
            "农业生产人员",
            "文体和娱乐用品制作人员",
            "无业或失业或离退休",
            "军人"
    };
    private String[] occupationIds = {
            "30100",
            "49900",
            "40100",
            "40400",
            "40300",
            "40500",
            "40600",
            "50100",
            "60900",
            "99999",
            "70000"
    };
    private CityDataBean cityDataBean;


    public void loadCityData(Context context) {

        if (cityDataBean!=null){
            getView ().loadCityDataSuccess (cityDataBean);
            return;
        }
        getView ().showLoading ("");
        Disposable disposable = Single.create (new SingleOnSubscribe<CityDataBean> () {
            @Override
            public void subscribe(SingleEmitter<CityDataBean> emitter) throws Exception {
                List<AreaItem> options1Items = new ArrayList<> ();
                List<List<SecondAreaItem>> options2Items = new ArrayList<List<SecondAreaItem>> ();
                List<List<List<ThirdAreaItem>>> options3Items = new ArrayList<List<List<ThirdAreaItem>>> ();
                InputStream is = null;
                String jsonData;
                try {
                    is = context.getAssets ().open ("AreaList.json");
                    byte[] bytes = new byte[is.available ()];
                    is.read (bytes);
                    jsonData = new String (bytes, "utf-8");
                } catch (IOException e) {
                    throw e;
                } finally {
                    if (is != null) {
                        try {
                            is.close ();
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }
                    }
                }
                Gson gson = new Gson ();
                List<AreaItem> areaItems = gson.fromJson (jsonData, new TypeToken<List<AreaItem>> () {
                }.getType ());
                for (int i = 0; i < areaItems.size (); i++) {
                    if ("0".equals (areaItems.get (i).parentid)) {
                        options1Items.add (areaItems.get (i));
                    }
                }
                List<SecondAreaItem> tempList1;
                for (int j = 0; j < options1Items.size (); j++) {
                    tempList1 = options1Items.get (j).children;
                    //解决台湾香港崩溃问题
                    if (tempList1.size () == 0) {
                        tempList1.add (new SecondAreaItem ());
                    }
                    options2Items.add (tempList1);
                }
                List<List<ThirdAreaItem>> tempListList;
                List<ThirdAreaItem> tempList2;
                for (int i = 0; i < options2Items.size (); i++) {
                    tempListList = new ArrayList<List<ThirdAreaItem>> ();
                    for (int j = 0; j < options2Items.get (i).size (); j++) {
                        tempList2 = options2Items.get (i).get (j).children;
                        if (tempList2 == null || tempList2.size () == 0) {
                            tempList2 = new ArrayList<ThirdAreaItem> ();
                            ThirdAreaItem temp = new ThirdAreaItem ();
                            temp.cityName = "";
                            temp.parentid = options2Items.get (i).get (j).codeid;
                            tempList2.add (temp);
                        }
                        tempListList.add (tempList2);
                    }
                    options3Items.add (tempListList);
                }
                emitter.onSuccess (new CityDataBean (options1Items, options2Items, options3Items));
            }
        }).subscribeOn (Schedulers.io ())
                .observeOn (AndroidSchedulers.mainThread ())
                .subscribe (new Consumer<CityDataBean> () {
                    @Override
                    public void accept(CityDataBean c) {
                        getView ().dismissLoading ();
                        cityDataBean=c;
                        getView ().loadCityDataSuccess (c);

                    }
                }, new Consumer<Throwable> () {
                    @Override
                    public void accept(Throwable throwable) {
                        getView ().dismissLoading ();
                        getView ().loadCityDataError ("-1","拉取城市列表失败");
                    }
                });


        compositeDisposable.add (disposable);
    }

    public void loadOccupation(boolean init){
        getView ().loadOccupationSuccess (init,occupationNames,occupationIds);
    }

    @Override
    public void onMvpDetachView(boolean retainInstance) {
        compositeDisposable.clear ();
        super.onMvpDetachView (retainInstance);
    }
}
