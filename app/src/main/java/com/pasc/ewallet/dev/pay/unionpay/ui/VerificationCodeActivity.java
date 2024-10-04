package com.pasc.ewallet.dev.pay.unionpay.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pasc.business.ewallet.base.EwalletBaseMvpActivity;
import com.pasc.business.ewallet.base.MultiPresenter;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.common.UserManager;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.ui.PayMainStandActivity;
import com.pasc.business.ewallet.business.pay.view.PayView;
import com.pasc.business.ewallet.openapi.PASCPay;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.ewallet.dev.R;
import com.pasc.ewallet.dev.pay.unionpay.model.param.SendSmsParam;
import com.pasc.ewallet.dev.pay.unionpay.presenter.GetVerifyCodePresenter;
import com.pasc.ewallet.dev.pay.unionpay.presenter.UnionPayPresenter;
import com.pasc.ewallet.dev.pay.unionpay.view.GetVerifyCodeView;
import com.pasc.ewallet.dev.pay.unionpay.widget.ClearEditText;
import com.pasc.ewallet.dev.pay.unionpay.widget.FormatEditText;
import com.pasc.ewallet.dev.utils.PayUtil;
import com.pasc.lib.pay.common.util.ToastUtils;

/**
 * 银行卡快捷支付
 */
public class VerificationCodeActivity extends EwalletBaseMvpActivity<MultiPresenter>
 implements GetVerifyCodeView, PayView {
  private PascToolbar toolbar;
  private FormatEditText etVerifyCode;
  private TextView tvGetVerifyCode;
  private Button btConfirmPay;
  public static int SMS_CODE_LEN = 6; //验证码默认6位
  private CountDownTimer counter;
  private final int countSeconds = 120;
  private long currentDownSeconds;
  private GetVerifyCodePresenter getVerifyCodePresenter;
  private UnionPayPresenter payPresenter;

  @Override protected int layoutResId() {
    return R.layout.activity_verification_code;
  }

  @Override protected void initView() {
    toolbar = findViewById (R.id.ewallet_activity_toolbar);
    initToolBar();
    etVerifyCode = findViewById(R.id.et_verify_code);
    tvGetVerifyCode = findViewById(R.id.tv_get_verify_code);
    btConfirmPay = findViewById(R.id.ewallet_confirm_pay);
  }

  @Override protected void initData(Bundle bundleData) {
    etVerifyCode.setEditTextChangeListener(new ClearEditText.EditTextChangeListener() {
      @Override
      public void afterChange(String s) {
        setLeftBtnNextClickable();
        showHint(etVerifyCode, s.length() == 0);
      }
    });
    tvGetVerifyCode.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        etVerifyCode.setText("");
        SendSmsParam sendSmsParam = new SendSmsParam(UserManager.getInstance().getMemberNo(),
            UserManager.getInstance().getCardNum(), PayUtil.PayType.UNION_BANK,
            UserManager.getInstance().getMchOrderNo(), UserManager.getInstance().getUnionOrderId(),
            UserManager.getInstance().getUnionOrderTime());
        getVerifyCodePresenter.getVerifyCode(sendSmsParam);
      }
    });
    btConfirmPay.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //确认支付
        payPresenter.payUnionCard(
            UserManager.getInstance().getMchOrderNo(),
            UserManager.getInstance().getMemberNo(),
            PayUtil.PayType.UNION_BANK,
            UserManager.getInstance().getUnionOrderId(),
            UserManager.getInstance().getUnionOrderTime(),
            UserManager.getInstance().getCardNum(),
            etVerifyCode.getOriginalText());
      }
    });
  }

  private void setLeftBtnNextClickable() {
    int pwd_len = etVerifyCode.getOriginalText().length();
    boolean pwd_has = (pwd_len == SMS_CODE_LEN);
    if (pwd_has) {
      btConfirmPay.setEnabled(true);
      btConfirmPay.setAlpha(1.0f);
    } else {
      btConfirmPay.setEnabled(false);
      btConfirmPay.setAlpha(0.3f);
    }
  }

  private void showHint(EditText editText, boolean ifHint) {
    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ifHint ? 16 : 18);
  }

  @Override protected MultiPresenter createPresenter() {
    MultiPresenter multiPresenter = new MultiPresenter ();
    multiPresenter.requestPresenter(getVerifyCodePresenter = new GetVerifyCodePresenter());
    multiPresenter.requestPresenter(payPresenter = new UnionPayPresenter());
    return multiPresenter;
  }

  protected void initToolBar() {
    toolbar.setTitle (getString (R.string.ewallet_bankcard_quick_pay));
    toolbar.enableUnderDivider (true);
    toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
      @Override
      public void onClick(View v) {
        finish ();
      }
    });
  }

  @Override public void getVerifyCodeSuccess() {
    startCounting();
  }

  @Override public void getVerifyCodeError(String code, String msg) {
      ToastUtils.toastMsg(msg);
  }

  public void startCounting() {
    if (this.counter == null) {
      this.counter = new CountDownTimer((long)(this.countSeconds * 1000), 1000L) {
        public void onTick(long l) {
          currentDownSeconds = l / 1000L;
          showTickingUI(currentDownSeconds);
        }

        public void onFinish() {
          currentDownSeconds = 0L;
          showTickFinishUI();
        }
      };
    }

    this.counter.start();
  }

  public void release() {
    if (this.counter != null) {
      this.counter.cancel();
    }

  }

  /**
   * 倒计时进行中
   * @param remainTime
   */
  public void showTickingUI(long remainTime) {
    tvGetVerifyCode.setText(getString(R.string.ewallet_retry_send_verify_code, remainTime));
    tvGetVerifyCode.setAlpha(0.4f);
    tvGetVerifyCode.setEnabled(false);
  }

  /**
   * 倒计时结束
   */
  public void showTickFinishUI() {
    tvGetVerifyCode.setText(getString(R.string.ewallet_retry_get_verify_code));
    tvGetVerifyCode.setAlpha(1.0f);
    tvGetVerifyCode.setEnabled(true);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    release();
  }

  @Override public void paySuccess(String payType, PayResp tradePayResp) {
    gotoActivity(PayMainStandActivity.class);
    finish();
    PASCPay.getInstance().notifyPayResult(PayUtil.PayType.UNION_BANK, PayResult.PAY_SUCCESS, "");
  }

  @Override public void payError(String payType, String code, String msg) {
    ToastUtils.toastMsg(msg);
  }

  @Override public void validPwdAndSendMsgCodeSuccess(QuickPaySendMsgResp paySendMsgResp) {

  }

  @Override public void validPwdAndSendMsgCodeError(String code, String msg) {
    ToastUtils.toastMsg(msg);
  }
}