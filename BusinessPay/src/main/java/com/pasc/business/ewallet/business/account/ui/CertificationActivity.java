package com.pasc.business.ewallet.business.account.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.account.view.CertificationView;
import com.pasc.business.ewallet.business.account.presenter.CertificationPresenter;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.bottompicker.CityPickerDialogFragment;
import com.pasc.business.ewallet.widget.dialog.bottompicker.ListPickerDialogFragment;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.CityDataBean;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.SecondAreaItem;
import com.pasc.business.ewallet.widget.dialog.bottompicker.bean.ThirdAreaItem;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.List;

/**
 * 身份认证信息
 */
public class CertificationActivity extends EwalletBaseMvpActivity<CertificationPresenter>
        implements CertificationView, View.OnClickListener {
    private TextView ewallet_certification_et_name;
    private TextView ewallet_certification_et_id;
    private Button ewallet_certification_next;

    private TextView ewallet_certification_et_location_desc;
    private CityPickerDialogFragment mCityPickerDialogFragment;
    private TextView ewallet_certification_et_occupation_desc;
    private ListPickerDialogFragment mListPickerDialogFragment;
    private int mOptions1 = 12;
    private int mOptions2;
    private int mOptions3;
    private int mPosition;
    private ScrollView ewallet_certification_sv;

    private String trueName;//用户真实姓名 脱敏的
    private String idCard;//身份证号 ，脱敏的
    private String addressCity = "";// 城市地址 province city area空格隔开
    private String addressDetail = "";//详细地址,length至少大于4
    private String occupationID = "";//职业,初始值

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType type) {
                if (type instanceof QuitAccountCreateEventType) {
                    //关闭
                    finish ();
                }
            }
        };
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_account_certification;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        ewallet_certification_et_name = findViewById (R.id.ewallet_certification_et_name);
        ewallet_certification_sv = findViewById (R.id.ewallet_certification_sv);
        ewallet_certification_et_id = findViewById (R.id.ewallet_certification_et_id);
        ewallet_certification_et_location_desc = findViewById (R.id.ewallet_certification_et_location_desc);
        EditText ewallet_certification_et_location_more = findViewById (R.id.ewallet_certification_et_location_more);
        ewallet_certification_et_occupation_desc = findViewById (R.id.ewallet_certification_et_occupation_desc);
        ewallet_certification_next = findViewById (R.id.ewallet_certification_next);

        toolbar.setTitle (getString (R.string.ewallet_certification_idcard));
        toolbar.enableUnderDivider (false);
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });

        ewallet_certification_next.setOnClickListener (this);
        ewallet_certification_et_location_desc.setOnClickListener (this);
        ewallet_certification_et_occupation_desc.setOnClickListener (this);


        ewallet_certification_et_location_more.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addressDetail = s.toString ().replace (" ", "");
                updateNextStatus ();
            }
        });

        //防止键盘挡住输入界面，移动scrollview来解决
        ewallet_certification_et_location_more.setOnFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ewallet_certification_sv.scrollBy (0, 300);
            }
        });
    }

    @Override
    protected void initData(Bundle bundleData) {
        trueName = bundleData.getString (BundleKey.User.key_name);
        idCard = bundleData.getString (BundleKey.User.key_idCardNumber);
        ewallet_certification_et_name.setHint (trueName);
        ewallet_certification_et_id.setHint (idCard);
        updateNextStatus ();
        mPresenter.loadOccupation (true);
    }

    @Override
    protected CertificationPresenter createPresenter() {
        return new CertificationPresenter ();
    }

    @Override
    public void onClick(View v) {
        if (v.getId () == R.id.ewallet_certification_et_location_desc) {
            //选择城市列表
            mPresenter.loadCityData (this);
        } else if (v.getId () == R.id.ewallet_certification_et_occupation_desc) {
            //选择职业
            mPresenter.loadOccupation (false);
        } else if (v.getId () == R.id.ewallet_certification_next) {
            boolean hadAddress = !Util.isEmpty (addressCity) && !Util.isEmpty (addressDetail)
                    && addressDetail.length () > 4;
            if (!hadAddress) {
                ToastUtils.toastMsg ("请输入正确的联系地址");
                return;
            }
            UserManager.getInstance ().setAddressCity (addressCity);
            UserManager.getInstance ().setAddressDetail (addressDetail);
            UserManager.getInstance ().setOccupation (occupationID);
            RouterManager.AccountRouter.gotoCertificateUpload (this);

        }
    }

    @Override
    public void loadCityDataSuccess(CityDataBean cityDataBean) {
        //添加级联选择dialog
        if (mCityPickerDialogFragment == null) {
            mCityPickerDialogFragment = new CityPickerDialogFragment.Builder ()
                    .setPicker (cityDataBean.options1Items,
                            cityDataBean.options2Items, cityDataBean.options3Items)
                    .setOnCloseListener (new OnCloseListener<CityPickerDialogFragment> () {
                        @Override
                        public void onClose(CityPickerDialogFragment d) {
                            d.dismiss ();
                        }
                    })
                    .setOnConfirmListener (new OnConfirmListener<CityPickerDialogFragment> () {
                        @Override
                        public void onConfirm(CityPickerDialogFragment d) {
                            d.dismiss ();
                            String province = cityDataBean.options1Items.get (d.getOptions1 ()).cityName;
                            String city = "";
                            String area = "";
                            List<SecondAreaItem> secondAreaItems = cityDataBean.options2Items.get (d.getOptions1 ());
                            if (secondAreaItems.size () > d.getOptions2 ()) {
                                city = secondAreaItems.get (d.getOptions2 ()).cityName;
                                List<ThirdAreaItem> thirdAreaItems = cityDataBean.options3Items.get (d.getOptions1 ()).get (d.getOptions2 ());
                                if (thirdAreaItems.size () > d.getOptions3 ()) {
                                    area = thirdAreaItems.get (d.getOptions3 ()).cityName;
                                }
                            }
                            addressCity = updateAddress (province, city, area);
                            ewallet_certification_et_location_desc.setText (addressCity);
                            ewallet_certification_et_location_desc.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
                            mOptions1 = d.getOptions1 ();
                            mOptions2 = d.getOptions2 ();
                            mOptions3 = d.getOptions3 ();
                            updateNextStatus ();

                        }
                    })
                    .setTitle (getString (R.string.ewallet_select_city))
                    .setCloseText (getString (R.string.ewallet_cancel))
                    .setConfirmText (getString (R.string.ewallet_confirm))
                    .setSelectOptions (12, 0, 0)//福建省，福州市，
                    .setCircling (false)
                    .build ();
        } else {
            mCityPickerDialogFragment.setOptions1 (mOptions1);
            mCityPickerDialogFragment.setOptions2 (mOptions2);
            mCityPickerDialogFragment.setOptions3 (mOptions3);
        }

        mCityPickerDialogFragment.show (getSupportFragmentManager (), "CityPickerDialogFragment");
    }

    private void updateNextStatus() {
        boolean checkFail = Util.isEmpty (trueName) || Util.isEmpty (idCard) || Util.isEmpty (addressCity)
                || Util.isEmpty (addressDetail) || Util.isEmpty (occupationID);
        if (checkFail /***|| (!Util.isEmpty (addressMore) && addressMore.replace (" ", "").length () <= 4)***/) {
            ewallet_certification_next.setEnabled (false);
        } else {
            ewallet_certification_next.setEnabled (true);
        }
    }

    private String updateAddress(String province, String city, String area) {
        StringBuilder sb = new StringBuilder ();
        if (!TextUtils.isEmpty (province)) {
            sb.append (province);
            if (!TextUtils.isEmpty (city)) {
                sb.append (" ").append (city);
                if (!TextUtils.isEmpty (area)) {
                    sb.append (" ").append (area);
                }
            }
            sb.append (" ");
        }
        return sb.toString ();
    }

    @Override
    public void loadCityDataError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void loadOccupationSuccess(boolean isInit, String[] occupationNames, String[] occupationIds) {
        if (isInit) {
            mPosition = 0;
            String occName = occupationNames[mPosition];
            ewallet_certification_et_occupation_desc.setText (occName);
            occupationID = occupationIds[mPosition];
            updateNextStatus ();
            return;
        }

        if (mListPickerDialogFragment == null) {
            mListPickerDialogFragment = new ListPickerDialogFragment.Builder ()
                    .setOnCloseListener (new OnCloseListener<ListPickerDialogFragment> () {
                        @Override
                        public void onClose(ListPickerDialogFragment dialogFragment) {
                            dialogFragment.dismiss ();
                        }
                    })
                    .setOnConfirmListener (new OnConfirmListener<ListPickerDialogFragment> () {
                        @Override
                        public void onConfirm(ListPickerDialogFragment dialogFragment) {
                            dialogFragment.dismiss ();
                            mPosition = dialogFragment.getPosition ();
                            String occName = occupationNames[mPosition];
                            ewallet_certification_et_occupation_desc.setText (occName);
                            ewallet_certification_et_occupation_desc.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
                            occupationID = occupationIds[mPosition];
                            updateNextStatus ();
                        }
                    })
                    .setTitle (getString (R.string.ewallet_select_occupation))
                    .setCloseText (getString (R.string.ewallet_cancel))
                    .setConfirmText (getString (R.string.ewallet_confirm))
                    .setItems (occupationNames, 0)
                    .setCircling (false)
                    .build ();
        } else {
            mListPickerDialogFragment.setPosition (mPosition);
        }
        mListPickerDialogFragment.show (getSupportFragmentManager (), "ListPickerDialogFragment");
    }

    @Override
    public void loadOccupationError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void showLoading(String msg) {
        super.showLoading (msg);
    }

    @Override
    public void dismissLoading() {
        super.dismissLoading ();
    }

    @Override
    protected boolean needSafeCheck() {
        return true;
    }
}
