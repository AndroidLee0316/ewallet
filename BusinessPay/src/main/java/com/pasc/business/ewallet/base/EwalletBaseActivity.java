package com.pasc.business.ewallet.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.common.customview.LoadingDialog;
import com.pasc.business.ewallet.common.event.BaseEventType;
import com.pasc.business.ewallet.common.event.EventBusListener;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitEventType;
import com.pasc.business.ewallet.common.event.TokenInvalidEventType;
import com.pasc.business.ewallet.common.utils.KeyBoardUtils;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.ConfirmDialogFragment;
import com.pasc.business.ewallet.widget.toolbar.PascToolbar;
import com.pasc.lib.pay.common.util.AntiHijackUtil;
import com.pasc.lib.pay.common.util.StatusBarUtils;
import com.pasc.lib.pay.common.util.ToastUtils;
import com.pasc.lib.pay.statistics.StatisticsManager;

/**
 * @author yangzijian
 * @date 2019/2/18
 * @des
 * @modify
 **/
@NotProguard
public abstract class EwalletBaseActivity extends AppCompatActivity {

    private Bundle bundleDate;
    private EventBusObserver mEventBusObserver = registerEventBus ();
    protected boolean needRegisterEventBus(){
        return true;
    }
    protected PascToolbar toolbar;

    public interface EventBusObserver {
        void handleMessage(BaseEventType eventType);
    }

    /**
     * 如果EventBusObserver 返回null，表示不注册eventBus；
     * EventBusObserver 赋值后，表示注册eventBus
     *
     * @return EventBusObserver
     */
    protected EventBusObserver registerEventBus() {
        return null;
    }

    protected boolean needToolBar() {
        return false;
    }

    protected abstract int layoutResId();

    protected abstract void initView();

    protected abstract void initData(Bundle bundleData);

    /**
     * 初始化其他
     */
    protected void setContViewBefore(Bundle savedInstanceState) {
    }

    /**
     * 初始化其他
     */
    protected void setContViewAfter(Bundle savedInstanceState) {
    }

    /**
     * needExtendLayout 返回true时使用  needExtendLayout 作为父布局  initLayout则作为子布局
     * 返回false 则默认用 initLayout
     *
     * @return
     */
    protected int getExtendLayout() {
        return -1;
    }

    /**
     * 拓展布局用 返回true 则 用 needExtendLayout initLayout作为 needExtendLayout, 反之用 initLayout
     *
     * @return
     */
    protected boolean needExtendLayout() {
        return false;
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        StatisticsManager.getInstance ().onPageStart (getClass ().getName ());
        enableSecure ();
        if (needRegisterEventBus ()) {
            EventBusManager.getDefault ().register (eventBusObserver);
        }
        setContViewBefore (savedInstanceState);
        super.onCreate (savedInstanceState);
        /*** 拓展布局用 返回true 则 用 needExtendLayout initLayout作为 needExtendLayout, 反之用 layoutResId ****/
        @LayoutRes int layout = needExtendLayout () && getExtendLayout () > 0 ? getExtendLayout () : layoutResId ();
        setContentView (layout);

        initTitleBar ();
        setContViewAfter (savedInstanceState);
        initView ();
        getBundleDate (savedInstanceState);
        statusBarColor ();
    }

    protected CharSequence toolBarTitle() {
        return "";
    }

    protected void initTitleBar() {
        toolbar = findViewById (R.id.ewallet_activity_toolbar);
        if (needToolBar ()) {
            if (toolbar != null) {
                toolbar.setTitle (toolBarTitle ());
                toolbar.addCloseImageButton ().setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        clickFinishActivity ();
                    }
                });
            }
        }
    }

    protected void statusBarColor() {
        int color = getResources ().getColor (R.color.ewallet_white_bg);//白色
        setImmersiveStatusBar (color, true);
    }

    EventBusListener eventBusObserver = new EventBusListener () {
        @Override
        public void handleMessage(BaseEventType type) {
            if (type instanceof QuitEventType) {
                //退出事件
                finish ();
            } else if (type instanceof TokenInvalidEventType) {
                TokenInvalidEventType t = (TokenInvalidEventType) type;
                if (t.mActivity != null && t.mActivity instanceof FragmentActivity) {
                    if (t.mActivity.getClass ().getName ().equals (this.getClass ().getName ())) {
                        showTokenInvalidTip (t.msg);
                    }
                }
            }else {
                if (mEventBusObserver != null) {
                    mEventBusObserver.handleMessage (type);
                }
            }
        }
    };

    /**
     * 如果开启沉浸式，就有效果
     *
     * @param statusBarcolor argb
     */
    public void setImmersiveStatusBar(@NonNull int statusBarcolor, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS & getWindow ().getAttributes ().flags)
                    == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {
                //沉浸了，巴拉巴拉
                // 控制状态栏颜色
                StatusBarUtils.setStatusBarColor (this, isDark);
                //沉浸式使用
                setFitsSystemWindows (this, true);
                //默认状态栏颜色为白色
                setStatusBarColor (statusBarcolor, this);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(@NonNull int statusColor, Activity activity) {
        Window window = activity.getWindow ();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //取消设置Window半透明的Flag
            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明 argb
            window.setStatusBarColor (statusColor);
        } else {
            //5.0以下，状态栏为灰色
            setStatusBarBackground_V19 (activity, 0xff5a595b);
        }

    }

    private int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources ();
        int resourceId = res.getIdentifier ("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize (resourceId);
        }
        return statusBarHeight;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setStatusBarBackground_V19(Activity activity, int color) {
        Window window = activity.getWindow ();
        window.addFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView ();
        View statusBarView = new View (window.getContext ());
        //获取statusBar高度
        int statusBarHeight = getStatusBarHeight (window.getContext ());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams (FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams (params);
        statusBarView.setBackgroundColor (color);
        decorViewGroup.addView (statusBarView);

        //设置标题栏下移
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById (android.R.id.content)).getChildAt (0);
        rootView.setFitsSystemWindows (true);
        rootView.setClipToPadding (true);
    }

    /**
     * 设置页面最外层布局 FitsSystemWindows 属性
     *
     * @param activity
     * @param value
     */
    private void setFitsSystemWindows(Activity activity, boolean value) {
        ViewGroup contentFrameLayout = activity.findViewById (android.R.id.content);
        View parentView = contentFrameLayout.getChildAt (0);
        if (parentView != null) {
            parentView.setFitsSystemWindows (value);
        }
    }

    /**
     * singleTop  singleTask 或者 singleInstance
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent (intent);
        setIntent (intent);
        getBundleDate (null);
    }

    /**
     * 获取传递的bundle数据
     * bundle数据来源有三个
     * 1：界面正常跳转来的intent中
     * 2：onNewIntent中重新传来的intent中
     * 3：界面被后台回收后回到界面的savedInstanceState中
     */
    private void getBundleDate(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            /****跳转携带过来的数据*****/
            if (getIntent () != null) {
                bundleDate = getIntent ().getExtras ();
            }
        } else {
            /****由于系统销毁而保存的数据*****/
            bundleDate = savedInstanceState;
        }

        if (bundleDate == null) {
            /**防止空指针 *****/
            bundleDate = new Bundle ();
        }
        initData (bundleDate);

    }


    /**
     * 调用时机
     * 1、当用户按下HOME键时。
     * 2、长按HOME键，选择运行其他的程序时。
     * 3、按下电源按键（关闭屏幕显示）时。
     * 4、从activity A中启动一个新的activity时。
     * 5、屏幕方向切换时，例如从竖屏切换到横屏时。
     * 总而言之，onSaveInstanceState的调用遵循一个重要原则，即当系统“未经你许可”时销毁了你的activity，
     * 则onSaveInstanceState会被系统调用，
     * 这是系统的责任，因为它必须要提供一个机会让你保存你的数据（当然你不保存那就随便你了）。
     *
     * @param savedInstanceState
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && bundleDate != null) {
            savedInstanceState.putAll (bundleDate);
        }
        super.onSaveInstanceState (savedInstanceState);
    }

    /**
     * 至于onRestoreInstanceState方法，需要注意的是，
     * onSaveInstanceState方法和onRestoreInstanceState方法“不一定”是成对的被调用的
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
        // 获取数据
    }

    /**
     * add loading dialog
     */
    protected LoadingDialog mLoadingDialog = null;

    private boolean isDestroy = false;

    public void showLoading() {
        showLoading (null, false);
    }

    public void showLoading(@StringRes int resId) {
        showLoading (getString (resId), false);
    }

    public void showLoading(String loadingTip) {
        showLoading (loadingTip, false);
    }

    public void showLoading(String loadingTip, boolean cancelAble) {
        if (isDestroy) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog (this);
            mLoadingDialog.progress (loadingTip);
        } else {
            mLoadingDialog.progress (loadingTip);
        }
        mLoadingDialog.setCanCancel (cancelAble);
        if (!mLoadingDialog.isShowing ()){
            mLoadingDialog.show ();
        }
    }

    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing ()) {
            mLoadingDialog.dismiss ();
        }
    }

    public void destroyLoadingDialog() {
        dismissLoading ();
        mLoadingDialog = null;
    }

    public void showErrorTip(String msg) {
        if (TextUtils.isEmpty (msg)) {
            return;
        }
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (msg)
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (getResources ().getColor (R.color.ewallet_color_333333))
                .setConfirmText (getString (R.string.ewallet_iknow))
                .setConfirmTextSize (18)
                .setHideCloseButton (true)
                .setConfirmTextColor (getResources ().getColor (R.color.ewallet_primary_btn_enable))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss ();
                    }
                })
                .build ();
        confirmDialogFragment.show (getSupportFragmentManager (), "showErrorTipActivity");
    }

    public void showTokenInvalidTip(@NonNull String tip) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder ()
                .setDesc (tip)
                .setDescLineSpacingExtra (4)
                .setDescSize (15)
                .setDescColor (getResources ().getColor (R.color.ewallet_color_333333))
                .setConfirmText (getString (R.string.ewallet_iknow))
                .setConfirmTextSize (18)
                .setHideCloseButton (true)
                .setConfirmTextColor (getResources ().getColor (R.color.ewallet_primary_btn_enable))
                .setOnConfirmListener (new OnConfirmListener<ConfirmDialogFragment> () {
                    @Override
                    public void onConfirm(ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss ();
                        //退出
                        EventBusManager.getDefault ().post (new QuitEventType ());
                        // 回调接入方Token失效
                        if (PayManager.getInstance ().getOnPayListener () != null) {
                            PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_TOKEN_INVALID, PASCPayResult.PASC_PAY_MSG_TOKEN_INVALID);
                        }
                        if (PayManager.getInstance ().getOnOpenListener () != null) {
                            // 回调Code统一
                            PayManager.getInstance ().getOnPayListener ().onPayResult (PASCPayResult.PASC_PAY_CODE_TOKEN_INVALID, PASCPayResult.PASC_PAY_MSG_TOKEN_INVALID);
                        }
                    }
                })
                .build ();
        confirmDialogFragment.show (getSupportFragmentManager (), "showTokenInvalidTipActivity");
    }

    @Override
    protected void onDestroy() {
        KeyBoardUtils.hideInputForce (this);
        StatisticsManager.getInstance ().onPageEnd (getClass ().getName ());
        super.onDestroy ();

        disableSecure ();
        if (needRegisterEventBus ()) {
            EventBusManager.getDefault ().unregister (eventBusObserver);

        }
        isDestroy = true;
        destroyLoadingDialog ();
    }


    @Override
    protected void onStop() {
        super.onStop ();
        attackCheck ();
    }

    public EwalletBaseActivity getActivity() {
        return this;
    }

    protected boolean needSafeCheck() {
        return false;
    }

    protected  boolean needSafeToast(){
        return true;
    }

    void enableSecure() {
        if (needSafeCheck ()) {
            getWindow ().setFlags (WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    void disableSecure() {
        if (needSafeCheck ()) {
            getWindow ().clearFlags (WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    void attackCheck() {
        if (needSafeCheck () && needSafeToast ()) {
            if (!AntiHijackUtil.checkActivity (this)) {
                ToastUtils.toastMsg (R.string.ewallet_toast_pay_goto_background);
            }
        }
    }

    protected String getSimpleName() {
        return getClass ().getSimpleName () + "->";
    }

    protected void clickFinishActivity() {
        finish ();
    }



    public void gotoActivity(Class<? extends Activity> targetClass) {
        gotoActivity (targetClass, null);
    }

    public void gotoActivity(Class<? extends Activity> targetClass, Bundle bundle) {
        gotoActivity (targetClass, bundle, -1);
    }

    public void gotoActivity(Class<? extends Activity> targetClass, int requestCode) {
        gotoActivity (targetClass, null, requestCode);

    }

    public void gotoActivity(Class<? extends Activity> targetClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent (getActivity (), targetClass);
        if (bundle != null) {
            intent.putExtras (bundle);
        }
        getActivity ().startActivityForResult (intent, requestCode);
    }

    @Override
    protected void onPause() {
        super.onPause ();
        StatisticsManager.getInstance ().onPause (this);
    }

    @Override
    protected void onResume() {
        super.onResume ();
        StatisticsManager.getInstance ().onResume (this);
    }

}
