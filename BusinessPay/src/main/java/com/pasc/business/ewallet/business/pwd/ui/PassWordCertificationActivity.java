package com.pasc.business.ewallet.business.pwd.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.base.MultiPresenter;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.account.net.resp.QueryMemberResp;
import com.pasc.business.ewallet.business.account.presenter.CreateAccountPresenter;
import com.pasc.business.ewallet.business.account.view.CreateAccountView;
import com.pasc.business.ewallet.business.pwd.presenter.PassWordCertificationPresenter;
import com.pasc.business.ewallet.business.pwd.view.PassWordCertificationView;
import com.pasc.business.ewallet.common.customview.SpaceEditText;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.QuitAccountCreateEventType;
import com.pasc.business.ewallet.common.filter.ChineseInputFilter;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.keyboard.EwalletKeyboardExtraView;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 忘记密码 身份验证
 *
 * @date 2019/7/15
 * @des
 * @modify
 **/
public class PassWordCertificationActivity extends EwalletBaseMvpActivity<MultiPresenter> implements
        View.OnClickListener, PassWordCertificationView, CreateAccountView {

    private SpaceEditText ewalletCertificationEtName;
    private ImageView ewalletCertificationDelName;
    private TextView ewalletCertificationTvId;
    private SpaceEditText ewalletCertificationEtId;
    private ImageView ewalletCertificationDelId;
    private Button ewalletCertificationNext;
    private EwalletKeyboardExtraView ewalletCertificationKv;
    private TextView ewallet_certification_tv_phone, ewallet_certification_tv_tip;
    private String name, idCardNumber;

    private String phoneNum;
    PassWordCertificationPresenter certificationPresenter;
    CreateAccountPresenter createAccountPresenter;
    private String setPwdTag;

    @Override
    protected EventBusObserver registerEventBus() {
        return new EventBusObserver () {
            @Override
            public void handleMessage(BaseEventType type) {
                if (type instanceof QuitAccountCreateEventType) {
                    finish ();
                }
            }
        };
    }

    @Override
    protected MultiPresenter createPresenter() {
        certificationPresenter = new PassWordCertificationPresenter ();
        createAccountPresenter = new CreateAccountPresenter ();
        MultiPresenter multiPresenter = new MultiPresenter ();
        multiPresenter.requestPresenter (
                certificationPresenter, createAccountPresenter
        );
        return multiPresenter;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_pay_certification;
    }

    @Override
    protected void initView() {
        PascToolbar toolbar = findViewById (R.id.ewallet_activity_toolbar);
        toolbar.setTitle (getString (R.string.ewallet_certification_idcard2));
        toolbar.enableUnderDivider (false);
        toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        });
        ewallet_certification_tv_tip = findViewById (R.id.ewallet_certification_tv_tip);
        ewalletCertificationEtName = findViewById (R.id.ewallet_certification_et_name);
        ewalletCertificationDelName = findViewById (R.id.ewallet_certification_del_name);
        ewalletCertificationTvId = findViewById (R.id.ewallet_certification_tv_id);
        ewalletCertificationEtId = findViewById (R.id.ewallet_certification_et_id);
        ewalletCertificationDelId = findViewById (R.id.ewallet_certification_del_id);
        ewalletCertificationNext = findViewById (R.id.ewallet_certification_next);
        ewalletCertificationKv = findViewById (R.id.ewallet_certification_kv);
        ewallet_certification_tv_phone = findViewById (R.id.ewallet_certification_tv_phone);
        ewalletCertificationEtName.setOnFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //有焦点，删除按钮显示；失去焦点，删除按钮隐藏
                if (hasFocus) {
                    if (ewalletCertificationEtName.getText ().length () > 0) {
                        //有数据
                        ewalletCertificationDelName.setVisibility (View.VISIBLE);
                    } else {
                        ewalletCertificationDelName.setVisibility (View.INVISIBLE);
                    }
                } else {
                    ewalletCertificationDelName.setVisibility (View.INVISIBLE);
                }
            }
        });

        ewalletCertificationKv.setEditText (this, ewalletCertificationEtId);
        ewalletCertificationKv.setFocusChangeListener (new View.OnFocusChangeListener () {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //有焦点，删除按钮显示；失去焦点，删除按钮隐藏
                if (hasFocus) {
                    if (ewalletCertificationEtId.getText ().length () > 0) {
                        //有数据
                        ewalletCertificationDelId.setVisibility (View.VISIBLE);
                    } else {
                        ewalletCertificationDelId.setVisibility (View.INVISIBLE);
                    }
                } else {
                    ewalletCertificationDelId.setVisibility (View.INVISIBLE);
                }
            }
        });

        int limit = 18;
        ewalletCertificationEtName.setFilters (new InputFilter[]{new ChineseInputFilter (limit)});
        ewalletCertificationEtName.delLastSpace (limit + 1);
        ewalletCertificationEtName.setTextChangeListener (new SpaceEditText.TextChangeListener () {
            @Override
            public void textChange(String text) {
                if (text.length () > 0) {
                    //有数据
                    ewalletCertificationDelName.setVisibility (View.VISIBLE);
                } else {
                    ewalletCertificationDelName.setVisibility (View.INVISIBLE);
                }
                updateNameChange (text);
            }
        });

        ewalletCertificationEtId.setFilters (new InputFilter[]{new InputFilter.LengthFilter (limit)});
        //不格式
        ewalletCertificationEtId.delLastSpace (limit + 1);
        ewalletCertificationEtId.setTextChangeListener (new SpaceEditText.TextChangeListener () {
            @Override
            public void textChange(String text) {
                if (text.length () > 0) {
                    //有数据
                    ewalletCertificationDelId.setVisibility (View.VISIBLE);
                } else {
                    ewalletCertificationDelId.setVisibility (View.INVISIBLE);
                }
                updateIdCardChange (text);
            }
        });
        ewalletCertificationDelName.setOnClickListener (this);
        ewalletCertificationDelId.setOnClickListener (this);
        ewalletCertificationNext.setOnClickListener (this);

        StatisticsUtils.kh_authorize ();
    }

    @Override
    protected void initData(Bundle bundleData) {
        phoneNum = bundleData.getString (BundleKey.User.key_phoneNum);
        setPwdTag = bundleData.getString (BundleKey.User.key_set_pwd_tag);
        if (Util.isEmpty (phoneNum)) {
            createAccountPresenter.queryMemberByMemberNo (UserManager.getInstance ().getMemberNo ());
        } else {
            ewallet_certification_tv_phone.setText (Util.formatPhoneNum (phoneNum));
        }
        checkNextStatus ();

        //
        ewallet_certification_tv_tip.setText (StatusTable.PassWordTag.fromForgetPwdTag.equals (setPwdTag)
                ? "请输入您的真实信息，以验证身份" : getString (R.string.ewallet_certification_input_tip2));
    }

    void updateNameChange(String str) {
        name = str.replace (" ", "");
        checkNextStatus ();
    }

    void updateIdCardChange(String str) {
        idCardNumber = str.replace (" ", "");
        ewalletCertificationEtId.setTextColor (getResources ().getColor (R.color.ewallet_color_333333));
        checkNextStatus ();
    }


    void checkNextStatus() {
        if ((!Util.isEmpty (idCardNumber) && idCardNumber.length () >= 15)
                && (!Util.isEmpty (name) && name.length () >= 2) && !Util.isEmpty (phoneNum)) {
            updateBtnStatus (true);
            //身份证 15 -18， 姓名 2 - 18
        } else {
            updateBtnStatus (false);
        }

    }

    void updateBtnStatus(boolean agree) {
        ewalletCertificationNext.setEnabled (agree);
    }

    @Override
    public void onClick(View v) {
        if (v == ewalletCertificationDelName) {
            ewalletCertificationEtName.setText ("");
        } else if (v == ewalletCertificationDelId) {
            ewalletCertificationEtId.setText ("");
        } else if (v == ewalletCertificationNext) {
            if (!Util.isValidIdCard (idCardNumber)) {
                ewalletCertificationEtId.setTextColor (getResources ().getColor (R.color.ewallet_error_highlight_text));
                return;
            }
            StatisticsUtils.kh_idnextstep ();
            certificationPresenter.validCert (UserManager.getInstance ().getMemberNo (), idCardNumber, name, StatusTable.getScenes (setPwdTag));
        }

    }


    @Override
    public void authenticationSuccess() {
        RouterManager.AccountRouter.gotoCreateAccountPhoneOtp (this, phoneNum, setPwdTag);
    }

    @Override
    public void authenticationError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void queryQueryMemberSuccess(QueryMemberResp memberResp) {
        phoneNum = memberResp.phone;
        ewallet_certification_tv_phone.setText (Util.formatPhoneNum (phoneNum));

    }

    @Override
    public void queryQueryMemberError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }
}
