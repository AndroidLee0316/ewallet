package com.pasc.business.ewallet.business.bankcard.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.business.BundleKey;
import com.pasc.business.ewallet.business.bankcard.adapter.IBankCardItem;
import com.pasc.business.ewallet.business.bankcard.presenter.BankCardDetailPresenter;
import com.pasc.business.ewallet.business.bankcard.view.BankDetailView;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.RefreshQuickCardEvent;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.lib.glide.GlideUtil;
import com.pasc.business.ewallet.widget.dialog.OnCloseListener;
import com.pasc.business.ewallet.widget.dialog.OnSingleChoiceListener;
import com.pasc.business.ewallet.widget.dialog.bottomchoice.BottomChoiceDialogFragment;
import com.pasc.business.ewallet.widget.dialognt.CommonDialog;
import com.pasc.lib.pay.common.util.ToastUtils;

import java.util.ArrayList;

/**
 * @date 2019/8/16
 * @des
 * @modify
 **/
public class BankCardDetailActivity extends EwalletBaseMvpActivity<BankCardDetailPresenter> implements BankDetailView {
    IBankCardItem iBankCardItem;
    private View ewalletBankDetailTopLayout;
    private ImageView ewalletBankDetailLogo;
    private TextView ewalletBankName;
    private TextView ewalletBankTypeName;
    private TextView ewalletBankNo;
    private TextView ewalletBankUserName;
    private TextView ewalletSingleLimitMoney;
    private TextView ewalletDayLimitMoney;
    private ImageView ewalletWaterMark,ewallet_bank_detail_bg;

    @Override
    protected boolean needToolBar() {
        return true;
    }

    @Override
    protected CharSequence toolBarTitle() {
        return "银行卡详情";
    }

    @Override
    protected BankCardDetailPresenter createPresenter() {
        return new BankCardDetailPresenter ();
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_activity_bankcard_detail;
    }

    @Override
    protected void initView() {
        ewalletBankDetailTopLayout = findViewById (R.id.ewallet_bank_detail_top_layout);
        ewalletBankDetailLogo = findViewById (R.id.ewallet_bank_detail_logo);
        ewalletBankName = findViewById (R.id.ewallet_bank_name);
        ewalletBankTypeName = findViewById (R.id.ewallet_bank_type_name);
        ewalletBankNo = findViewById (R.id.ewallet_bank_no);
        ewalletBankUserName = findViewById (R.id.ewallet_bank_user_name);
        ewalletSingleLimitMoney = findViewById (R.id.ewallet_single_limit_money);
        ewalletDayLimitMoney = findViewById (R.id.ewallet_day_limit_money);
        ewalletWaterMark = findViewById (R.id.ewallet_water_mark);
        ewallet_bank_detail_bg=findViewById (R.id.ewallet_bank_detail_bg);
    }

    @Override
    protected void initData(Bundle bundleData) {

        iBankCardItem = (IBankCardItem) bundleData.getSerializable (BundleKey.Pay.key_bankCard_info);

        if (iBankCardItem == null) {
            finish ();
            return;
        }
        if (!iBankCardItem.isSafeCard ()) {
            toolbar.addRightImageButton (R.drawable.ewallet_ic_toolbar_more)
                    .setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {
                            deleteDialog ();
                        }
                    });
        }
        GlideUtil.loadImage (this, ewalletBankDetailLogo,
                iBankCardItem.logo (),
                R.drawable.ewallet_ic_no_bank_card, R.drawable.ewallet_ic_no_bank_card);
        ewalletBankName.setText (iBankCardItem.bankName ());
        ewalletBankNo.setText (iBankCardItem.cardNo ());
        String userName="";
        if (iBankCardItem.isSafeCard ()){
            userName=Util.isEmpty (iBankCardItem.userName ()) ? UserManager.getInstance ().getSafeName () : Util.formatName (iBankCardItem.userName ());
        }else {
            userName=iBankCardItem.userName ();
        }
        ewalletBankUserName.setText (userName);
//        GlideUtil.loadBg (this, ewalletBankDetailTopLayout,
//                iBankCardItem.bankBackground (),
//                R.drawable.ewallet_bg_no_bank_card, R.drawable.ewallet_bg_no_bank_card);
        GlideUtil.loadImage (this, ewallet_bank_detail_bg,
                iBankCardItem.bankBackground (),
                R.drawable.ewallet_bg_no_bank_card, R.drawable.ewallet_bg_no_bank_card);
        GlideUtil.loadImage (this, ewalletWaterMark,
                iBankCardItem.bankWaterMark (),
                R.drawable.ewallet_ic_bank_card_bg_logo, R.drawable.ewallet_ic_bank_card_bg_logo);
        ewalletBankTypeName.setText (iBankCardItem.cardTypeName ());
        ewalletSingleLimitMoney.setText (iBankCardItem.singleLimit ());
        ewalletDayLimitMoney.setText (iBankCardItem.singleDayLimit ());

        if (Util.isEmpty (iBankCardItem.bankWaterMark ())) {
            ewalletWaterMark.setVisibility (View.GONE);
        } else {
            ewalletWaterMark.setVisibility (View.VISIBLE);
            GlideUtil.loadImage (this,ewalletWaterMark,iBankCardItem.bankWaterMark (),R.drawable.ewallet_ic_bank_card_bg_logo,R.drawable.ewallet_ic_bank_card_bg_logo);
        }
    }

    void deleteDialog() {
        BottomChoiceDialogFragment.Builder builder = new BottomChoiceDialogFragment.Builder ();
        ArrayList<CharSequence> items = new ArrayList<> ();
        String source = getResources ().getString (R.string.ewallet_delete_bankcard);
        SpannableString spannableString = new SpannableString (source);
        spannableString.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.ewallet_error_highlight_text)),
                0, source.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        items.add (spannableString);
        builder.setItems (items)
                .setCloseText (getString (R.string.ewallet_cancel))
                .setOnSingleChoiceListener (new OnSingleChoiceListener<BottomChoiceDialogFragment> () {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onSingleChoice(BottomChoiceDialogFragment dialogFragment, int position) {
                        dialogFragment.dismiss ();
                        CommonDialog commonDialog = new CommonDialog (getActivity ());
                        commonDialog.setContent (getString (R.string.ewallet_is_really_delete_bankcard))
                                .setButton1 (getString (R.string.ewallet_cancel), "#22C8D8")
                                .setButton2 (getString (R.string.ewallet_delete_sure))
                                .setOnButtonClickListener (new CommonDialog.OnButtonClickListener () {
                                    @Override
                                    public void button2Click() {
                                        mPresenter.unBindQuickCard (iBankCardItem.cardKey (),UserManager.getInstance ().getMemberNo ());
                                    }
                                })
                                .show ();
                    }
                })
                .setOnCloseListener (new OnCloseListener<BottomChoiceDialogFragment> () {
                    @Override
                    public void onClose(BottomChoiceDialogFragment dialogFragment) {
                        dialogFragment.dismiss ();
                    }
                })
                .build ()
                .show (getSupportFragmentManager (), "deleteDialogTag");
    }

    @Override
    public void unBindQuickCardSuccess() {
        EventBusManager.getDefault ().post (new RefreshQuickCardEvent ());
        finish ();
        ToastUtils.toastMsg (R.drawable.ewallet_toast_success,"删卡成功");
    }

    @Override
    public void unBindQuickCardError(String code, String msg) {
        ToastUtils.toastMsg (msg);
    }
}
