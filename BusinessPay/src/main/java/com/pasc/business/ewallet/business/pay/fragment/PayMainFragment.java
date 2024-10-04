package com.pasc.business.ewallet.business.pay.fragment;

import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.base.MultiPresenter;
import com.pasc.business.ewallet.business.RouterManager;
import com.pasc.business.ewallet.business.StatusTable;
import com.pasc.business.ewallet.business.bankcard.net.resp.QuickPaySendMsgResp;
import com.pasc.business.ewallet.business.pay.adapter.PayTypeAdapter;
import com.pasc.business.ewallet.business.pay.net.resp.PayContextResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayResp;
import com.pasc.business.ewallet.business.pay.net.resp.PayTypeBean;
import com.pasc.business.ewallet.business.pay.net.resp.QueryOrderResp;
import com.pasc.business.ewallet.business.pay.presenter.PayMainPresenter;
import com.pasc.business.ewallet.business.pay.presenter.PayPresenter;
import com.pasc.business.ewallet.business.pay.presenter.QueryOrderPresenter;
import com.pasc.business.ewallet.business.pay.view.PayMainView;
import com.pasc.business.ewallet.business.pay.view.PayView;
import com.pasc.business.ewallet.business.pay.view.QueryOrderView;
import com.pasc.business.ewallet.business.pay.wechat.WechatPayUtil;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.EventBusListener;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.ThirdPayEvent;
import com.pasc.business.ewallet.common.utils.LogUtil;
import com.pasc.business.ewallet.common.utils.StatisticsUtils;
import com.pasc.business.ewallet.common.utils.TypeFaceUtil;
import com.pasc.business.ewallet.common.utils.Util;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.business.ewallet.result.PayResult;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.adapter.base.BaseQuickAdapter;
import com.pasc.lib.pay.common.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019/7/26
 * @des
 * @modify
 **/
public class PayMainFragment extends BasePayFragment<MultiPresenter> implements PayMainView,
    QueryOrderView, PayView {
    private static final String TAG = "PayMainFragment";
    //顶部操作栏
    private PascToolbar mToolbar, selectToolBar;

    private LinearLayout mPayInformationLayout;
    //支付给
    //private TextView mPayToTV;
    //支付金额
    private TextView mPayNumTV;
    //推荐
    private TextView mMainActivityTagTV;
    //推荐内容
    private TextView mMainActivityContentTV;
    //支付方式选择
    private RecyclerView mainListView, typeListView;
    //确定支付按钮
    private TextView mSurePayTV;
    //支付方式选中位置
    private int mPayTypeSelection = 0;
    private View mNoneCardTip;
    View rvNoneCardAdd, ivNoneCardArrow;
    private List<PayTypeBean> mainList = new ArrayList<> ();
    PayTypeAdapter mainAdapter;

    private List<PayTypeBean> selectList = new ArrayList<> ();
    PayTypeAdapter selectAdapter;

    View mainContent;
    View typeListContent;
    private boolean isAnim = false;
    private boolean weChatHasCallBack = false;
    private boolean weChatCallSuccess = false;

    private long orderAmount; //订单金额，单位：分
    private String merchantName; //商户名称

    private PayMainPresenter payMPresenter;
    private PayPresenter payPresenter;
    private QueryOrderPresenter queryOrderPresenter;
    //银联充值单号
    private String rechargeOrderNo;
    private View footer;
    boolean hideShowMore=hideShowMore ();

    @Override
    protected MultiPresenter createPresenter() {
        MultiPresenter multiPresenter = new MultiPresenter ();
        multiPresenter.requestPresenter (payMPresenter = new PayMainPresenter ());
        multiPresenter.requestPresenter (payPresenter = new PayPresenter ());
        multiPresenter.requestPresenter (queryOrderPresenter = new QueryOrderPresenter ());
        return multiPresenter;
    }

    @Override
    protected int layoutResId() {
        return R.layout.ewallet_pay_main_fragment;
    }

    @Override
    protected void initView() {
        EventBusManager.getDefault ().register (eventBusObserver);
        mToolbar = findViewById (R.id.ewallet_pay_main_dialog_toolbar);
        selectToolBar = findViewById (R.id.ewallet_pay_main_type_select_toolbar);
        mPayInformationLayout = findViewById(R.id.ll_pay_information);
        //mPayToTV = findViewById (R.id.ewallet_pay_main_dialog_pay_to_tv);
        mPayNumTV = findViewById (R.id.ewallet_pay_main_dialog_pay_money_num);
        mMainActivityTagTV = findViewById (R.id.ewallet_pay_main_dialog_pay_type_remind2);
        mMainActivityContentTV = findViewById (R.id.ewallet_pay_main_dialog_pay_type_remind);
        mainListView = findViewById (R.id.ewallet_payType_rv);
        mSurePayTV = findViewById (R.id.ewallet_pay_main_dialog_commit_btn);
        mNoneCardTip = findViewById (R.id.ewallet_pay_main_none_card);
        mPayNumTV.setTypeface (TypeFaceUtil.getDinAlternateBold (getActivity ()));
        rvNoneCardAdd = findViewById (R.id.rv_none_card_add);
        ivNoneCardArrow = findViewById (R.id.iv_none_card_arrow);
        typeListView = findViewById (R.id.ewallet_payType_more_rv);
        mainContent = findViewById (R.id.ewallet_main_list_ll);
        typeListContent = findViewById (R.id.ewallet_type_list_ll);
        mToolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                getPayCallbackView ().payCancel ();
            }
        });
        mainAdapter = new PayTypeAdapter (mainList, hideShowMore);
        mainListView.setLayoutManager (new LinearLayoutManager (getActivity ()));
        mainListView.setAdapter (mainAdapter);
        mainAdapter.setOnItemClickListener (new BaseQuickAdapter.OnItemClickListener () {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PayTypeBean payTypeBean = mainList.get (position);
                if (!StatusTable.PayType.SELECT_MORE.equalsIgnoreCase(payTypeBean.payType)) {
                    if (!payTypeBean.disable) {
                        mainAdapter.setSelection (position);
                        mPayTypeSelection = position;
                        if (StatusTable.PayType.UNION_BANK.equals(payTypeBean.payType)) {
                            //银联无跳转支付需要调用接口并跳转页面
                            PayResp payResp = new PayResp();
                            payResp.returnValue = memberNo;
                            payResp.orderno = mchOrderNo;
                            PayManager.getInstance()
                                .getOnPayTypeClickListener()
                                .onPayTypeChoose(getActivity(), StatusTable.PayType.UNION_BANK, payResp);
                        }
                    }
                } else {
                    //选择更多支付方式
                    //1.修改title的内容

                    TextView titleView = mToolbar.getTitleView();
                    if (titleView != null) {
                        //titleView.setTypeface(TypeFaceUtil.getDinAlternateBold(getActivity()));
                        //设置文字为粗体
                        titleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    //需要使用半角的人民币符号，全角的“￥”会受系统的影响，在有些手机上显示一条横线
                    mToolbar.setTitle("¥" + Util.doublePoint (orderAmount,2));
                    //2.隐藏付款给和订单金额
                    mPayInformationLayout.setVisibility(View.GONE);
                    //3.显示全部支付方式
                    mainList.clear();
                    mainList.addAll(selectList);
                    mainAdapter.notifyDataSetChanged();
                }
            }
        });
        selectToolBar.addLeftImageButton (R.drawable.ewallet_toolbar_ic_back)
                /*** selectToolBar.addCloseImageButton ()***/.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                onBackPressed ();
            }
        });
        selectAdapter = new PayTypeAdapter (selectList, true);
        typeListView.setLayoutManager (new LinearLayoutManager (getActivity ()));

        if (StatusTable.enableQuickCard) {
            footer = LayoutInflater.from (getActivity ()).inflate (R.layout.ewallet_add_more_card_item, null);
            selectAdapter.addFooterView (footer);
            footer.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    RouterManager.BankCardRouter.gotoAddQuickCard (getActivity ());
                }
            });
        }
        typeListView.setAdapter (selectAdapter);
        selectAdapter.setOnItemClickListener (new BaseQuickAdapter.OnItemClickListener () {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PayTypeBean payTypeBean = selectList.get (position);
                if (!payTypeBean.disable) {
                    mPayTypeSelection = position;
                    updateMainData (payTypeBean);
                    onBackPressed ();
                }
            }
        });
        mainContent.setVisibility (View.VISIBLE);
        typeListContent.setVisibility (View.GONE);

        mSurePayTV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (selectList.size () > 0) {
                    int pos = mPayTypeSelection;
                    if (!(selectList.size () > pos)) {
                        pos = 0;
                    }
                    PayTypeBean payType = selectList.get (pos);
                    commitOrder (payType);
                }
            }
        });

        isAnim = false;
        weChatHasCallBack = false;
        weChatCallSuccess = false;
    }

    @Override
    public void onBackPressed() {
        if (typeListContent.getVisibility () == View.VISIBLE) {
            if (!isAnim) {
                closeSelectAnim ();
            }
        } else {
            getPayCallbackView ().payCancel ();
        }
    }

    void commitOrder(PayTypeBean payType) {
        if (payType == null) {
            return;
        }
        getPayCallbackView().setChannel(payType.channel);
        getPayCallbackView ().setPayType (payType.payType);
        getPayCallbackView ().setPayTypeName (payType.payTypeName);
        getPayCallbackView ().setCardKey (payType.cardKey);
        getPayCallbackView ().setQuickCardPhone (payType.bindMobile);
        getPayCallbackView ().setBankCardName (payType.getPayTypeName ());
        if (isPayMode ()) {
            //支付
            if (orderIsValid ()) {
                weChatHasCallBack = false;
                if (StatusTable.PayType.BALANCE.equalsIgnoreCase (payType.payType)) {
                    //余额支付
                    getPayCallbackView ().switchToPage (FragmentFactory.PwdPosition);
                    StatisticsUtils.syt_main_balance ();
                } else if (StatusTable.PayType.UNION_BANK.equalsIgnoreCase (payType.payType)) {
                    //银联无跳转支付
                    PayResp payResp = new PayResp();
                    payResp.returnValue = memberNo;
                    payResp.orderno = mchOrderNo;
                    PayManager.getInstance()
                        .getOnPayTypeClickListener()
                        .onPayTypeChoose(getActivity(), StatusTable.PayType.UNION_BANK, payResp);
                } else {
                    //第三方支付
                    payPresenter.pay (mchOrderNo, memberNo, payType.payType, "");
                }
            }
        } else {
            // 充值
            payPresenter.recharge (memberNo, payType.payType, money, mchOrderNo);
        }
    }

    public void setData(PayContextResp payContextBean, boolean isRefresh) {
        //if (mPayToTV == null) {
        //    return;
        //}
        String desc = null;
        String title = null;
        if (footer!=null){
            footer.setVisibility (payContextBean.userAuthFlag?View.VISIBLE:View.GONE);
        }
        if (payContextBean.discountContext != null) {
            desc = payContextBean.discountContext.desc;
            title = payContextBean.discountContext.title;
        }
        mMainActivityContentTV.setText (desc);
        mMainActivityTagTV.setText (title);
        mMainActivityTagTV.setVisibility (Util.isEmpty (title) ? View.GONE : View.VISIBLE);
        mMainActivityContentTV.setVisibility (Util.isEmpty (desc) ? View.GONE : View.VISIBLE);

        mainContent.setVisibility (View.VISIBLE);
        typeListContent.setVisibility (View.GONE);
        List<PayTypeBean> typeBeans = payContextBean.list;

        //if (isPayMode ()) {
        //    mPayToTV.setText (String.format (getString (R.string.ewallet_pay_to_pre), payContextBean.merchantName));
        //} else {
        //    mPayToTV.setText (payContextBean.merchantName);
        //}
        mPayNumTV.setText (payContextBean.getPayFee ());
        orderAmount = payContextBean.orderAmount;
        merchantName = payContextBean.merchantName;
        mainList.clear ();
        selectList.clear ();
        if (typeBeans != null && typeBeans.size () > 0) {
            //数组越界了
            if (!(typeBeans.size () >= mPayTypeSelection)) {
                mPayTypeSelection = 0;
            }

            //获取一个能用的支付渠道
            boolean hasEnable = false;
            //没可用的话，就去找一个 能用的
            for (int pos = 0; pos < typeBeans.size (); pos++) {
                PayTypeBean payTypeBean = typeBeans.get (pos);
                if (!payTypeBean.disable) {
                    mPayTypeSelection = pos;
                    hasEnable = true;
                    break;
                }
            }
            selectList.addAll (typeBeans);
            if (hideShowMore){
                mainList.clear();
                if (typeBeans.size() > 3) {
                    //获取前三个支付方式
                    mainList.addAll(typeBeans.subList(0, 3));
                    //添加选择更多支付方式
                    PayTypeBean payTypeBean = new PayTypeBean();
                    payTypeBean.payType = StatusTable.PayType.SELECT_MORE;
                    payTypeBean.payTypeName = "选择更多支付方式";
                    mainList.add(payTypeBean);
                } else {
                    mainList.addAll(typeBeans);
                }
            }
            if (!hasEnable) {
                if (mainAdapter != null) {
                    mainAdapter.notifyDataSetChanged ();
                }
                if (selectAdapter != null) {
                    mainAdapter.notifyDataSetChanged ();
                }
                hasNoEnablePayChannel ();
                return;
            }
            if (!hideShowMore) {
                updateMainData (typeBeans.get (mPayTypeSelection));
            }
            mainListView.setVisibility (View.VISIBLE);
            mNoneCardTip.setVisibility (View.GONE);
            mSurePayTV.setEnabled (true);
        } else {

            hasNoEnablePayChannel ();
        }
        if (mainAdapter != null) {
            mainAdapter.setSelection(mPayTypeSelection);
        }
        if (selectAdapter != null) {
            selectAdapter.setSelection (mPayTypeSelection);
            //selectAdapter.notifyDataSetChanged ();
        }
        if (selectList.size () > 0) {
//            刷新数据时，回到第一个
            typeListView.getLayoutManager ().scrollToPosition (0);
        }
    }

    void hasNoEnablePayChannel() {
        mNoneCardTip.setVisibility (View.VISIBLE);
        mainListView.setVisibility (View.GONE);
        ivNoneCardArrow.setVisibility (View.GONE);
        mSurePayTV.setEnabled (false);
    }


    private void updateMainData(PayTypeBean payType) {
        mainList.clear ();
        mainList.add (payType);
        if (mainAdapter != null) {
            mainAdapter.notifyDataSetChanged ();
        }
    }

    void showSelectAnim() {
        typeListContent.setVisibility (View.VISIBLE);
        mainContent.setVisibility (View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation (getActivity (), R.anim.ewallet_right_to_left_in);
        animation.setAnimationListener (new Animation.AnimationListener () {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                typeListContent.setVisibility (View.VISIBLE);
                mainContent.setVisibility (View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        typeListContent.startAnimation (animation);
    }

    void closeSelectAnim() {
        isAnim = true;
        typeListContent.setVisibility (View.VISIBLE);
        mainContent.setVisibility (View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation (getActivity (), R.anim.ewallet_left_to_right_out);
        animation.setAnimationListener (new Animation.AnimationListener () {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                typeListContent.setVisibility (View.GONE);
                mainContent.setVisibility (View.VISIBLE);
                isAnim = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        typeListContent.startAnimation (animation);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        EventBusManager.getDefault ().unregister (eventBusObserver);
    }

    EventBusListener eventBusObserver = new EventBusListener () {
        @Override
        public void handleMessage(BaseEventType eventType) {
            if (eventType instanceof ThirdPayEvent) {
                ThirdPayEvent thirdPayEvent = (ThirdPayEvent) eventType;
                String wechat = StatusTable.Channel.WECHAT + "_" + StatusTable.PayType.WECHAT;
                if (wechat.equals(thirdPayEvent.payType)) {
                    //微信支付
                    if (thirdPayEvent.payStatus == PayResult.PAY_SUCCESS) {
                        weChatCallSuccess = false;
                        weChatHasCallBack = false;
                        thirdPayQuery(true, thirdPayEvent.payType, true);
                        return;
                    }
                    weChatHasCallBack = true;
                } else {
                    if (thirdPayEvent.payStatus == PayResult.PAY_SUCCESS) {
                        thirdPayQuery(true, thirdPayEvent.payType, true);
                    } else {
                        ToastUtils.toastMsg(thirdPayEvent.payMsg);
                    }
                }
            }
        }
    };


    @Override
    public void paySuccess(String payType, PayResp payBean) {
        String alipay = StatusTable.Channel.ALIPAY + "_" + StatusTable.PayType.ALIPAY;
        String wechat = StatusTable.Channel.WECHAT + "_" + StatusTable.PayType.WECHAT;
        //通过channel和payType来唯一确定一种支付方式
        String fullPayType = getChannel() + "_" + getPayType();
        if (!isPayMode ()) {
            rechargeOrderNo = payBean.rechargeOrderNo;
        } else {
            rechargeOrderNo = null;
        }
        if (alipay.equals (fullPayType)) {
            //支付宝
            String orderInfo = payBean.returnValue;
            payMPresenter.aliPay (getActivity (), orderInfo);
        } else if (wechat.equals (fullPayType)) {
            //微信支付
            if (!WechatPayUtil.isWechatInstalled (getActivity ())) {
                ToastUtils.toastMsg (getString (R.string.ewallet_toast_wechat_uninstall));
                return;
            }
            payMPresenter.weChatPay (getActivity (), payBean);
        } else {
            PayManager.getInstance()
                .getOnPayTypeClickListener()
                .onPayTypeChoose(getActivity(), fullPayType, payBean);
        }
        /*
        else if (StatusTable.PayType.SNPAY.equals (payType)) {
            //苏宁
            String orderInfo = payBean.suningResponse;
            payMPresenter.snPay (getActivity (), orderInfo);
        } else if (StatusTable.PayType.CMBCHINAPAY.equals (payType)) {
            //招行支付
            String orderInfo = payBean.cmbchinaResponse;
            CMBRequest request = new CMBRequest();
            //支付、协议、领券等业务功能等请求参数，具体内容由业务功能给出具体内容，SDK透传，会将该字段信息Post给对应功能页面
            try {
                //SDK接口的requestData（json字符串先做url编码再进行拼接）
                request.requestData = "charset=utf-8&jsonRequestData=" + URLEncoder.encode(orderInfo, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //h5Url与CMBJumpUrl至少有一个赋值，app已经安装时要跳转到的招商银行APP具体功能的url
            request.CMBJumpAppUrl = CmbPayUtil.CMB_JUMP_URL;
            //h5Url与CMBJumpUrl至少有一个赋值，app没有安装时在商户APP打开H5页面
            if (Constants.IS_DEBUG) {
                request.CMBH5Url = CmbPayUtil.H5_URL_DEBUG;
            } else {
                request.CMBH5Url = CmbPayUtil.H5_URL_RELEASE;
            }
            //业务功能类型，SDK透传，支付默认上送pay
            request.method = "pay";
            PayMainStandActivity payMainStandActivity = (PayMainStandActivity) getActivity();
            if (payMainStandActivity != null && payMainStandActivity.getCmbApi() != null) {
                payMainStandActivity.getCmbApi().sendReq(request);
            }
        } else if (StatusTable.PayType.SWIFT.equals (payType)) {
            //威富通
            RequestMsg msg = new RequestMsg();
            msg.setTokenId(payBean.swiftResponse);
            msg.setAppId("wx174a5f32c5770a12"); //传入微信开放平台生成的应用ID
            msg.setMiniProgramId("gh_4d85c1138220"); //传入小程序原始Id
            msg.setMiniProgramType(0); //小程序开发模式：0正式，1开发，2体验
            msg.setIsBack("1"); //微信开放平台移动应用没有配置包名的时候isBack配置为0，否则为1
            msg.setTradeType(MainApplication.PAY_MINI_PROGRAM);
            msg.setPathVersion("1"); //小程序版本,旧版本设置为0，默认为1
            PayPlugin.unifiedH5Pay(getActivity(), msg);
        } else if (StatusTable.PayType.WECHATJSAPI.equals (payType)) {
            //微信小程序支付
            String orderInfo = payBean.wechatJsapiResponse;
            Gson gson = new Gson();
            MiniProgramResp miniProgramResp = gson.fromJson(orderInfo, MiniProgramResp.class);
            String appId = miniProgramResp.getSkipAppId(); // 填应用AppId
            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = miniProgramResp.getSkipMiniprogramAppId(); // 填小程序原始id
            req.path = miniProgramResp.getPath();   //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = miniProgramResp.getMiniprogramType();// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        } else if (StatusTable.PayType.UNION_ALIPAYJSAPI.equals (payType)) {
            //银联支付宝
            if (UnifyUtils.hasInstalledAlipayClient(getContext())) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(payBean.returnValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject == null) {
                    LogUtil.logd(TAG, "appPayRequest alipay jsonObject为null");
                    return;
                }
                JSONObject appPayRequestObject = jsonObject.optJSONObject("appPayRequest");
                if (appPayRequestObject != null) {
                    LogUtil.logd(TAG, "appPayRequest alipay--> " + appPayRequestObject.toString());
                    UnionPayUtil.payAliPayMiniPro(getContext(), appPayRequestObject.toString());
                }
            } else {
                Toast.makeText(getContext(), "未安装支付宝客户端", Toast.LENGTH_SHORT).show();
            }
        } else if (StatusTable.PayType.UNION_WECHATJSAPI.equals (payType)) {
            //银联微信
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(payBean.returnValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject == null) {
                LogUtil.logd(TAG, "appPayRequest wechat jsonObject为null");
                return;
            }
            JSONObject appPayRequestObject = jsonObject.optJSONObject("appPayRequest");
            if (appPayRequestObject != null) {
                LogUtil.logd(TAG, "appPayRequest wechat--> " + appPayRequestObject.toString());
                UnionPayUtil.payWX(getContext(), appPayRequestObject.toString());
            }
        }
         */
    }

    @Override
    public void payError(String payType, String code, String msg) {
        ToastUtils.toastMsg (msg);
    }

    @Override
    public void validPwdAndSendMsgCodeSuccess(QuickPaySendMsgResp paySendMsgResp) {

    }

    @Override
    public void validPwdAndSendMsgCodeError(String code, String msg) {

    }

    @Override
    public void aliPaySuccess(String msg) {
        LogUtil.loge ("aliPaySuccess->" + msg);
        thirdPayQuery (true, StatusTable.Channel.ALIPAY + "_" + StatusTable.PayType.ALIPAY, false);

    }

    @Override
    public void aliPayError(String msg, boolean isCancel) {
        if (!isCancel) {
            ToastUtils.toastMsg (msg);
        }
        LogUtil.loge ("aliPayError->" + msg);

    }

    @Override
    public void weChatLauncherSuccess(String msg) {
        LogUtil.loge ("weChatPaySuccess->" + msg);
        weChatCallSuccess = true;

    }

    @Override
    public void weChatLauncherError(String msg) {
        ToastUtils.toastMsg (msg);
        LogUtil.loge ("weChatPayError->" + msg);
        weChatCallSuccess = false;

    }

    @Override
    public void queryOrderStatusSuccess(QueryOrderResp orderResp) {
        if (StatusTable.PayStatus.SUCCESS.equalsIgnoreCase (orderResp.status)) {

            if (isPayMode ()) {
                //第三方支付成功后，不需要跳成功界面
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_SUCCESS, PASCPayResult.PASC_PAY_MSG_SUCCESS);
                }
                ToastUtils.toastMsg (R.drawable.ewallet_toast_success, "支付成功");
                LogUtil.loge ("PayMainFragment - >" + PASCPayResult.PASC_PAY_MSG_SUCCESS);
                String channelStatistics = orderResp.channelDesc;
                StatisticsUtils.syt_paymentsuccess (channelStatistics);
                getPayCallbackView ().closePayActivity (true);
                return;
            } else {
                //第三方，充值成功后需要跳成功界面
                //orderResp.needCallback = isPayMode ();
                //if (Util.isEmpty (orderResp.merchantName)) {
                //    orderResp.merchantName = "我的钱包";
                //}
                //RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), orderResp);
                //关闭 充值界面
//                EventBusManager.getDefault ().post (new QuitRechargeEvent ());
                if (PayManager.getInstance ().getOnPayListener () != null) {
                    PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_SUCCESS, PASCPayResult.PASC_PAY_MSG_SUCCESS);
                }
                StatisticsUtils.cz_topupsuccess ();
            }

        } else {
            //跳转成功
            //if (orderResp.amount == 0) {
            //    orderResp.amount = orderAmount;
            //}
            //if (!isPayMode ()) {
            //    if (Util.isEmpty (orderResp.merchantName)) {
            //        orderResp.merchantName = "我的钱包";
            //    }
            //} else {
            //    if (Util.isEmpty (orderResp.merchantName)) {
            //        orderResp.merchantName = merchantName;
            //    }
            //}
            //orderResp.needCallback = isPayMode ();
            //RouterManager.PayRouter.gotoPaySuccessResult (getActivity (), orderResp);
//            if (!isPayMode ()) {
//                //关闭 充值界面
//                EventBusManager.getDefault ().post (new QuitRechargeEvent ());
//            }
            if (PayManager.getInstance ().getOnPayListener () != null) {
                PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_WAITING, PASCPayResult.PASC_PAY_MSG_WAITING);
            }
        }
        getPayCallbackView ().closePayActivityDelay ();
    }


    @Override
    public void queryOrderStatusError(String code, String msg) {
        //QueryOrderResp queryOrderResp = new QueryOrderResp ();
        ////跳转失败
        //queryOrderResp.statusDesc = msg;
        //queryOrderResp.needCallback = isPayMode ();
//        if (!isPayMode ()) {
//            //关闭 充值界面
//            EventBusManager.getDefault ().post (new QuitRechargeEvent ());
//        }
//        RouterManager.PayRouter.gotoPayErrorResult (getActivity (), queryOrderResp);
        if (PayManager.getInstance ().getOnPayListener () != null) {
            PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_FAILED, PASCPayResult.PASC_PAY_MSG_FAILED);
        }
        getPayCallbackView ().closePayActivityDelay ();

    }

    @Override
    public void queryOrderStatusTimeOut() {
        ToastUtils.toastMsg (R.string.ewallet_toast_network_error_and_retry);
    }

    @Override
    public void onResume() {
        super.onResume ();
        if (weChatHasCallBack) {
            // 微信有回调
            weChatHasCallBack = false;
            weChatCallSuccess = false;
        } else if (weChatCallSuccess) {
            //微信没有回调，但是吊起来了。因为微信直接被杀了，直接回来。不知道有没有支付成功
            weChatHasCallBack = false;
            weChatCallSuccess = false;
            thirdPayQuery (false, StatusTable.PayType.WECHAT, false);
        }

    }

    void thirdPayQuery(boolean isThirdPaySuccess, String payType, boolean delayQuery){
        if (Util.ignoreAnim ()){
            mToolbar.postDelayed (new Runnable () {
                @Override
                public void run() {
                    LogUtil.logd("JZB", "thirdPayQuery, ignoreAnim queryOrderStatus");
                    queryOrderPresenter.queryOrderStatus (mchOrderNo, rechargeOrderNo, payType, tradeType (), isThirdPaySuccess, true);
                }
            },200);
            return;
        }
        if (delayQuery) {
            //避免上一个页面还未关闭，当查询订单状态时loading框显示不出来。延时100毫秒，保证上一个页面已经关闭，此时loading框能显示出来。
            mToolbar.postDelayed (new Runnable () {
                @Override
                public void run() {
                    LogUtil.logd("JZB", "thirdPayQuery, delay queryOrderStatus");
                    queryOrderPresenter.queryOrderStatus (mchOrderNo, rechargeOrderNo, payType, tradeType (), isThirdPaySuccess, true);
                }
            },100);
        } else {
            LogUtil.logd("JZB", "thirdPayQuery, normal queryOrderStatus");
            queryOrderPresenter.queryOrderStatus (mchOrderNo, rechargeOrderNo, payType, tradeType (), isThirdPaySuccess, true);
        }
    }

    /***是否隐藏显示更多，直接在主页显示 所有支付方式****/
    protected boolean hideShowMore(){
        //return BuildConfig.currentBuildType== PayBuildConfig.jhPayBuildType;
        return true;
    }

    public String getPayType() {
        String payType = "";
        if (mainList != null) {
            PayTypeBean payTypeBean = mainList.get(mPayTypeSelection);
            if (payTypeBean != null) {
                payType = payTypeBean.payType;
            }
        }
        return payType;
    }

    public String getChannel() {
        String channel = "";
        if (mainList != null) {
            PayTypeBean payTypeBean = mainList.get(mPayTypeSelection);
            if (payTypeBean != null) {
                channel = payTypeBean.channel;
            }
        }
        return channel;
    }
}
