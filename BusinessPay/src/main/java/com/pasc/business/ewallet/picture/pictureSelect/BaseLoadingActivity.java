package com.pasc.business.ewallet.picture.pictureSelect;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import com.pasc.business.ewallet.widget.dialog.loading.LoadingDialogFragment;

/**
 * Created by duyuan797 on 17/10/3.
 */

public class BaseLoadingActivity extends AppCompatActivity {

    private LoadingDialogFragment mLoadingDialog;
    private boolean isDestroy = false;

    public boolean isActivityDestroy() {
        return isDestroy;
    }

    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(@StringRes int resId) {
        showLoading(getString(resId));
    }

    public void showLoading(String loadingTip) {
        if (isDestroy){
            return;
        }
        if (mLoadingDialog == null){
            mLoadingDialog = new LoadingDialogFragment.Builder()
                    .setMessage(loadingTip)
                    //.setRotateImageRes(R.drawable.ewallet_ic_circle_loading)
                    .build();
        } else {
            mLoadingDialog.updateMessage(loadingTip);
        }
        mLoadingDialog.show(getSupportFragmentManager(), "LoadingDialogFragment");
    }

    public void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        dismissLoading();
        mLoadingDialog = null;
    }
}
