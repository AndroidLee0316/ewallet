package com.pasc.business.ewallet.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.common.customview.LoadingDialog;
import com.pasc.business.ewallet.common.event.EventBusManager;
import com.pasc.business.ewallet.common.event.QuitEventType;
import com.pasc.business.ewallet.inner.PayManager;
import com.pasc.business.ewallet.result.PASCPayResult;
import com.pasc.business.ewallet.widget.dialog.OnConfirmListener;
import com.pasc.business.ewallet.widget.dialog.common.ConfirmDialogFragment;

/**
 * Created by  yzj
 * on 2017/4/27.
 */

public abstract class EwalletBaseFragment extends Fragment {
    protected View rootView;

    /**
     * 结合needExtendLayout 返回true时使用  getExtendLayout子布局 作为父布局  initLayout则作为子布局
     * 返回false 则默认用 initLayout
     *
     * @return
     */
    protected int getExtendLayout() {
        return -1;
    }

    protected abstract int layoutResId();

    protected abstract void initView();

    /**
     * 初始化其他
     */
    protected void setContViewAfter() {
    }
    protected void setContViewBefore() {
    }
    protected abstract void initData(Bundle bundleData);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent ();
            if (parent != null) {
                parent.removeView (rootView);
            }
        } else {
            @LayoutRes int layout = needExtendLayout () && getExtendLayout () > 0 ? getExtendLayout () : layoutResId ();
            rootView = inflater.inflate (layout, null);
        }
        setContViewBefore ();
        initView ();
        setContViewAfter ();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        getBundleDate (getArguments ());

    }

    protected void getBundleDate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            /**防止空指针 *****/
            savedInstanceState = new Bundle ();
        }
        initData (savedInstanceState);
    }

    public <T extends View> T findViewById(int id) {
        if (rootView == null) {
            return null;
        }
        return (T) rootView.findViewById (id);
    }

    public void showErrorTip(String msg) {
        if (isDestroy || TextUtils.isEmpty (msg)){
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
        confirmDialogFragment.show (getActivity ().getSupportFragmentManager (), "showErrorTipFragment");
    }

    public void showTokenInvalidTip(@NonNull String msg){
        if (isDestroy || TextUtils.isEmpty (msg)){
            return;
        }
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment.Builder()
                .setDesc(msg)
                .setDescLineSpacingExtra(4)
                .setDescSize(15)
                .setDescColor(getResources().getColor(R.color.ewallet_color_333333))
                .setConfirmText(getString(R.string.ewallet_iknow))
                .setConfirmTextSize(18)
                .setHideCloseButton(true)
                .setConfirmTextColor(getResources().getColor(R.color.ewallet_primary_btn_enable))
                .setOnConfirmListener(new OnConfirmListener<ConfirmDialogFragment>() {
                    @Override
                    public void onConfirm (ConfirmDialogFragment confirmDialogFragment) {
                        confirmDialogFragment.dismiss();
                        //退出
                        EventBusManager.getDefault().post(new QuitEventType ());
                        // 回调接入方Token失效
                        if (PayManager.getInstance().getOnPayListener() != null) {
                            PayManager.getInstance().getOnPayListener().onPayResult(PASCPayResult.PASC_PAY_CODE_TOKEN_INVALID, PASCPayResult.PASC_PAY_MSG_TOKEN_INVALID);
                        }
                        if (PayManager.getInstance().getOnOpenListener() != null){
                            // 回调Code统一
                            PayManager.getInstance().getOnPayListener().onPayResult(PASCPayResult.PASC_PAY_CODE_TOKEN_INVALID, PASCPayResult.PASC_PAY_MSG_TOKEN_INVALID);
                        }
                    }
                })
                .build();
        confirmDialogFragment.show(getActivity ().getSupportFragmentManager(), "showTokenInvalidTipFragment");
    }

    /**
     * 拓展布局用 返回true 则 用 needExtendLayout initLayout作为 getExtendLayout子布局, 反之用 initLayout
     *
     * @return
     */
    protected boolean needExtendLayout() {
        return false;
    }


    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
    @Override
    public void onDetach() {
        super.onDetach ();
        //修复在androidx中Fragment空指针问题
        //try {
        //    Field childFragmentManager = Fragment.class.getDeclaredField ("mChildFragmentManager");
        //    childFragmentManager.setAccessible (true);
        //    childFragmentManager.set (this, null);
        //} catch (Exception e) {
        //    e.printStackTrace ();
        //}
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        isDestroy = true;
        destroyLoadingDialog ();

    }
    /**
     * add loading dialog
     */
    private LoadingDialog mLoadingDialog = null;

    private boolean isDestroy = false;

    public void showLoading() {
        showLoading(null,false);
    }

    public void showLoading(@StringRes int resId) {
        showLoading(getString(resId),false);
    }

    public void showLoading(String loadingTip) {
        showLoading (loadingTip,false);
    }

    public void showLoading(String loadingTip,boolean cancelAble) {

        if (getActivity () !=null && getActivity () instanceof EwalletBaseActivity){
            ((EwalletBaseActivity) getActivity ()).showLoading (loadingTip,cancelAble);
            return;
        }
        if (isDestroy){
            return;
        }
        if (mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(getActivity ());
            mLoadingDialog.progress(loadingTip);
        } else {
            mLoadingDialog.progress(loadingTip);
        }
        mLoadingDialog.setCanCancel (cancelAble);
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        if (getActivity () !=null && getActivity () instanceof EwalletBaseActivity){
            ((EwalletBaseActivity) getActivity ()).dismissLoading ();
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void destroyLoadingDialog(){
        if (getActivity () !=null && getActivity () instanceof EwalletBaseActivity){
            return;
        }
        dismissLoading ();
        mLoadingDialog=null;
    }

    public void gotoActivity(Class<? extends Activity> targetClass) {
        gotoActivity (targetClass, null);
    }

    public void gotoActivity(Class<? extends Activity> targetClass, Bundle bundle) {
        gotoActivity (targetClass, bundle, -1);
    }

    public void gotoActivity(Class<? extends Activity> targetClass, int requestCode) {
        gotoActivity (targetClass, null, -1);

    }

    public void gotoActivity(Class<? extends Activity> targetClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent (getActivity (), targetClass);
        if (bundle != null) {
            intent.putExtras (bundle);
        }
        getActivity ().startActivityForResult (intent, requestCode);
    }
}
