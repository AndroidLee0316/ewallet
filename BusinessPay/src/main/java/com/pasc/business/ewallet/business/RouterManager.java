package com.pasc.business.ewallet.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.business.account.ui.CreateAccountActivity;
import com.pasc.business.ewallet.business.account.ui.otp.CreateAccountPhoneOtpActivity;
import com.pasc.business.ewallet.business.bankcard.adapter.IBankCardItem;
import com.pasc.business.ewallet.business.bankcard.ui.AddMainCardActivity;
import com.pasc.business.ewallet.business.bankcard.ui.BankCardDetailActivity;
import com.pasc.business.ewallet.business.bankcard.ui.BankCardListActivity;
import com.pasc.business.ewallet.business.bankcard.ui.BindChangeBankMainCardActivity;
import com.pasc.business.ewallet.business.bankcard.ui.otp.AddMainCardPhoneOtpActivity;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.home.net.QueryBalanceResp;
import com.pasc.business.ewallet.business.home.ui.EwalletHomeActivity;
import com.pasc.business.ewallet.business.logout.net.resp.MemberValidResp;
import com.pasc.business.ewallet.business.logout.ui.WalletLogoutDisableActivity;
import com.pasc.business.ewallet.business.logout.ui.WalletLogoutEnableActivity;
import com.pasc.business.ewallet.business.logout.ui.WalletLogoutSelectActivity;
import com.pasc.business.ewallet.business.logout.ui.WalletLogoutSuccessActivity;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.pay.ui.PayMainStandActivity;
import com.pasc.business.ewallet.business.pay.ui.PayResultFailedActivity;
import com.pasc.business.ewallet.business.pay.ui.PayResultSuccessActivity;
import com.pasc.business.ewallet.business.pay.ui.SignMainStandActivity;
import com.pasc.business.ewallet.business.pwd.ui.ModifyPwdSuccessActivity;
import com.pasc.business.ewallet.business.pwd.ui.PassWordCertificationActivity;
import com.pasc.business.ewallet.business.pwd.ui.PassWordModifyActivity;
import com.pasc.business.ewallet.business.pwd.ui.PayManageActivity;
import com.pasc.business.ewallet.business.pwd.ui.SetPassWordActivity;
import com.pasc.business.ewallet.business.pwd.ui.VerifyPassWordActivity;
import com.pasc.business.ewallet.business.rechargewithdraw.net.resp.WithdrawResp;
import com.pasc.business.ewallet.business.rechargewithdraw.ui.RechargeActivity;
import com.pasc.business.ewallet.business.rechargewithdraw.ui.WithdrawActivity;
import com.pasc.business.ewallet.business.rechargewithdraw.ui.WithdrawSuccessActivity;
import com.pasc.business.ewallet.business.traderecord.net.resp.AvailBalanceBean;
import com.pasc.business.ewallet.business.traderecord.ui.BalanceDetailActivity;
import com.pasc.business.ewallet.business.traderecord.ui.BalanceListActivity;
import com.pasc.business.ewallet.business.traderecord.ui.BillDetailActivity;
import com.pasc.business.ewallet.business.traderecord.ui.BillListActivity;
import com.pasc.business.ewallet.business.traderecord.ui.BillListSearchActivity;
import com.pasc.business.ewallet.business.traderecord.ui.BillMonthActivity;
import com.pasc.business.ewallet.common.webview.PascPayWebViewActivity;

/**
 * @date 2019/7/1
 * @des
 * @modify
 **/
@NotProguard
public class RouterManager {
    /**
     * 跳转首页
     *
     * @param context
     */
    public static void gotoHome(Context context) {
        Intent intent = new Intent (context, EwalletHomeActivity.class);
        startActivity (context, intent);
    }

    /**
     * 跳转网页
     *
     * @param context
     * @param title
     * @param url
     */
    public static void gotoWeb(Context context, String title, String url) {
        Intent intent = new Intent (context, PascPayWebViewActivity.class);
        intent.putExtra (BundleKey.Web.key_title, title);
        intent.putExtra (BundleKey.Web.key_url, url);
        startActivity (context, intent);
    }


    public static class AccountRouter extends RouterManager {
        /**
         * 新建账户
         *
         * @param context
         * @param memberNo
         */
        public static void gotoCreateAccount(Context context, String memberNo) {
            Intent intent = new Intent (context, CreateAccountActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            startActivity (context, intent);
        }

        /**
         * 图片信息上传
         *
         * @param context
         */
        public static void gotoCertificateUpload(Context context) {
        }

        public static void gotoCreateAccountPhoneOtp(Context context, String phoneNum, String setPwdTag) {
            Intent intent = new Intent (context, CreateAccountPhoneOtpActivity.class);
            intent.putExtra (BundleKey.User.key_set_pwd_tag, setPwdTag);
            intent.putExtra (BundleKey.User.key_phoneNum, phoneNum);
            startActivity (context, intent);
        }

        public static void gotoRecharge(Context context) {
            Intent intent = new Intent (context, RechargeActivity.class);
            startActivity (context, intent);
        }


        public static void gotoWithdraw(Context context, QueryBalanceResp balanceResp) {
            Intent intent = new Intent (context, WithdrawActivity.class);
            intent.putExtra (BundleKey.Pay.key_balance, balanceResp);
            startActivity (context, intent);
        }
        public static void gotoWithdraw(Context context, QueryBalanceResp balanceResp,boolean fromLogout) {
            Intent intent = new Intent (context, WithdrawActivity.class);
            intent.putExtra (BundleKey.Pay.key_balance, balanceResp);
            intent.putExtra (BundleKey.Common.key_flag_tag, fromLogout);
            startActivity (context, intent);
        }
        public static void gotoWithdrawSuccess(Context context, WithdrawResp resp) {
            Intent intent = new Intent (context, WithdrawSuccessActivity.class);
            intent.putExtra (BundleKey.Pay.key_withdraw_info, resp);
            startActivity (context, intent);
        }

    }
    @NotProguard
    public static class BankCardRouter extends RouterManager {


        public static void gotoAddMainCard(Context context) {
            Intent intent = new Intent (context, AddMainCardActivity.class);
            startActivity (context, intent);
        }

        public static void gotoBindMainCard(Context context) {
            Intent intent = new Intent (context, BindChangeBankMainCardActivity.class);
            startActivity (context, intent);
        }

        public static void gotoAddMainCardPhoneOtp(Context context, String cardNum, String phoneNum,
                                                   boolean isBindCard) {
            Intent intent = new Intent (context, AddMainCardPhoneOtpActivity.class);
            intent.putExtra (BundleKey.User.key_bindCardNo, cardNum);
            intent.putExtra (BundleKey.User.key_phoneNum, phoneNum);
            intent.putExtra (BundleKey.User.key_flag_bind, isBindCard);
            startActivity (context, intent);

        }

        public static void gotoBankCardList(Context context) {
            Intent intent = new Intent (context, BankCardListActivity.class);
            startActivity (context, intent);
        }

        public static void gotoBankCardDetail(Context context, IBankCardItem bankCardItem) {
            Intent intent = new Intent (context, BankCardDetailActivity.class);
            intent.putExtra (BundleKey.Pay.key_bankCard_info, bankCardItem);
            startActivity (context, intent);
        }



        public static void gotoAddQuickCard(Context context) {
            PassWordRouter.gotoVerifyPassWord (context, context.getString (R.string.ewallet_add_bankcard)
                    , StatusTable.VerifyTag.ADD_QUICK_CARD);
        }
    }

    public static class PassWordRouter extends RouterManager {


        /**
         * 密码支付管理
         *
         * @param context
         */
        public static void gotoPayManager(Context context) {
            startActivity (context, new Intent (context, PayManageActivity.class));

        }
        public static void gotoVerifyPassWord(Context context, String title, String verifyTag) {
            Intent intent = new Intent (context, VerifyPassWordActivity.class);
            intent.putExtra (BundleKey.Common.key_title, title);
            intent.putExtra (BundleKey.Common.key_verify_tag, verifyTag);
            startActivity (context, intent);
        }
        /**
         * 设置密码
         *
         * @param context
         * @param validateCode
         */
        public static void gotoSetPassWord(Context context, String validateCode, String setPwdTag) {
            Intent intent = new Intent (context, SetPassWordActivity.class);
            intent.putExtra (BundleKey.User.key_validateCode, validateCode);
            intent.putExtra (BundleKey.User.key_set_pwd_tag, setPwdTag);
            startActivity (context, intent);
        }

        /**
         * 忘记密码
         *
         * @param context
         */
        public static void gotoForgetPwd(Context context) {
            gotoCertification (context, UserManager.getInstance ().getPhoneNum (), StatusTable.PassWordTag.fromForgetPwdTag);
        }

        public static void gotoCreateCertification(Context context, String phoneNum) {
            gotoCertification (context, phoneNum, StatusTable.PassWordTag.fromNormalCreateAccountTag);
        }

        public static void gotoCertification(Context context, String phoneNum, String setPwdTag) {
            Intent intent = new Intent (context, PassWordCertificationActivity.class);
            intent.putExtra (BundleKey.User.key_set_pwd_tag, setPwdTag);
            intent.putExtra (BundleKey.User.key_phoneNum, phoneNum);
            startActivity (context, intent);
        }

        /**
         * 修改密码成功
         *
         * @param context
         */
        public static void gotoChangePwdSuccess(Context context) {
            Intent intent = new Intent (context, ModifyPwdSuccessActivity.class);
            startActivity (context, intent);
        }

        /***
         * 修改密码
         * @param context
         */
        public static void gotoModifyPwd(Context context) {
            Intent intent = new Intent (context, PassWordModifyActivity.class);
            startActivity (context, intent);

        }
    }

    public static class PayRouter extends RouterManager {

        public static void gotoSign(Context context, String memberNo, String channel, String sceneId) {
            Intent intent = new Intent (context, SignMainStandActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            intent.putExtra (BundleKey.Pay.key_channel, channel);
            intent.putExtra (BundleKey.Pay.key_sceneId, sceneId);
            startActivity (context, intent);
        }

        public static void gotoSign(Context context, String memberNo, String merchantNo, String channel,
            String scheme, String sceneId) {
            Intent intent = new Intent (context, SignMainStandActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            intent.putExtra (BundleKey.Pay.key_channel, channel);
            intent.putExtra (BundleKey.Pay.key_sceneId, sceneId);
            intent.putExtra (BundleKey.Pay.key_merchantNo, merchantNo);
            intent.putExtra (BundleKey.Pay.key_scheme, scheme);
            startActivity (context, intent);
        }

        public static void gotoPay(Context context, String memberNo, String merchantNo, String mchOrderNo) {
            Intent intent = new Intent (context, PayMainStandActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            intent.putExtra (BundleKey.Pay.key_merchantNo, merchantNo);
            intent.putExtra (BundleKey.Pay.key_mchOrderNo, mchOrderNo);
            intent.putExtra (BundleKey.Pay.key_pay_mode, StatusTable.PayMode.payMode);
            startActivity (context, intent);
        }
        public static void gotoPay(Context context, String memberNo, String merchantNo, String mchOrderNo,String option) {
            Intent intent = new Intent (context, PayMainStandActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            intent.putExtra (BundleKey.Pay.key_merchantNo, merchantNo);
            intent.putExtra (BundleKey.Pay.key_mchOrderNo, mchOrderNo);
            intent.putExtra (BundleKey.Pay.key_payOption, option);
            intent.putExtra (BundleKey.Pay.key_pay_mode, StatusTable.PayMode.payMode);
            startActivity (context, intent);
        }
        public static void gotoRechargePay(Context context, String memberNo, String mchOrderNo, long amount) {
            Intent intent = new Intent (context, PayMainStandActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            intent.putExtra (BundleKey.Pay.key_mchOrderNo, mchOrderNo);
            intent.putExtra (BundleKey.Pay.key_money, amount);
            intent.putExtra (BundleKey.Pay.key_pay_mode, StatusTable.PayMode.rechargeMode);
            startActivity (context, intent);
        }

        public static void gotoPaySuccessResult(Context context, QueryOrderResp orderResp) {
            Intent intent = new Intent (context, PayResultSuccessActivity.class);
            intent.putExtra (BundleKey.Pay.key_query_order_resp, orderResp);
            startActivity (context, intent);


        }

        public static void gotoPayErrorResult(Context context, QueryOrderResp orderResp) {
            Intent intent = new Intent (context, PayResultFailedActivity.class);
            intent.putExtra (BundleKey.Pay.key_query_order_resp, orderResp);
            startActivity (context, intent);
        }
    }
    @NotProguard
    public static class BalanceBillRouter extends RouterManager {

        public static void gotoBillMonth(Context context, String memberNo, int year, int month) {
            Intent intent = new Intent (context, BillMonthActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            intent.putExtra (BundleKey.Trade.key_year, year);
            intent.putExtra (BundleKey.Trade.key_month, month);
            startActivity (context, intent);

        }

        public static void gotoBillDetail(Context context, String mchOrderNo, String orderNo, String tradeType) {
            Intent intent = new Intent (context, BillDetailActivity.class);
            intent.putExtra (BundleKey.Pay.key_mchOrderNo, mchOrderNo);
            intent.putExtra (BundleKey.Pay.key_orderNo, orderNo);
            intent.putExtra (BundleKey.Pay.key_tradeType, tradeType);
            startActivity (context, intent);
        }

        /**
         * 跳转 账单列表
         *
         * @param context
         */
        public static void gotoBillList(Context context, String memberNo) {
            Intent intent = new Intent (context, BillListActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            startActivity (context, intent);
        }

        /***
         * 账单搜索
         * @param context
         */
        public static void gotoBillSearch(Context context, String memberNo) {
            Intent intent = new Intent (context, BillListSearchActivity.class);
            intent.putExtra (BundleKey.Pay.key_memberNo, memberNo);
            startActivity (context, intent);
        }

        /**
         * 余额详情
         *
         * @param context
         * @param balanceBean
         */
        public static void gotoBalanceDetail(Context context, AvailBalanceBean balanceBean) {
            Intent intent = new Intent (context, BalanceDetailActivity.class);
            intent.putExtra (BundleKey.Trade.key_balanceBean, balanceBean);
            startActivity (context, intent);
        }

        /***
         * 余额列表
         * @param context
         */
        public static void gotoBalanceList(Context context) {
            startActivity (context, new Intent (context, BalanceListActivity.class));
        }
    }

    public static class LogoutRouter extends RouterManager{
        public static void gotoLogoutEnable(Context context){
            Intent intent = new Intent (context, WalletLogoutEnableActivity.class);
            startActivity (context, intent);
        }

        public static void gotoLogoutDisable(Context context, MemberValidResp memberValidResp){
            Intent intent = new Intent (context, WalletLogoutDisableActivity.class);
            intent.putExtra (BundleKey.Logout.key_member_valid_info,memberValidResp);
            startActivity (context, intent);
        }

        public static void gotoLogoutSelect(Context context){
            Intent intent = new Intent (context, WalletLogoutSelectActivity.class);
            startActivity (context, intent);
        }
        public static void gotoLogoutSuccess(Context context){
            Intent intent = new Intent (context, WalletLogoutSuccessActivity.class);
            startActivity (context, intent);
        }
    }

    private static void startActivity(Context context, Intent intent) {
        if (context == null) {
            return;
        }
        if (!(context instanceof Activity)) {
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (intent);
    }
}
