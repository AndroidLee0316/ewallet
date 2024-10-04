package com.pasc.business.ewallet.common.customview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;

/**
 * @author yangzijian
 * @date 2019/3/5
 * @des
 * @modify
 **/
public class StatusView extends FrameLayout {
    private LinearLayout statusLoading;
    private LinearLayout statusEmpty;
    private LinearLayout statusError;
    private TextView btnFooterRetry;
    private TextView tvLoadingText;
    private ImageView ivEmptyIcon;
    private TextView tvEmptyText;

    private ImageView ivErrorIcon;
    private TextView tvErrorText;

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    private View contentView;

    private IReTryListener tryListener;

    public void setTryListener(IReTryListener tryListener) {
        this.tryListener = tryListener;
    }

    private void assignViews() {
        statusLoading = findViewById (R.id.status_loading);
        statusEmpty = findViewById (R.id.status_empty);
        statusError = findViewById (R.id.status_error);
        btnFooterRetry = findViewById (R.id.btn_footer_retry);

        tvLoadingText = findViewById (R.id.tv_loading_text);

        ivEmptyIcon = findViewById (R.id.iv_empty_icon);
        tvEmptyText = findViewById (R.id.tv_empty_text);

        ivErrorIcon = findViewById (R.id.iv_error_icon);
        tvErrorText = findViewById (R.id.tv_error_text);


        btnFooterRetry.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                if (tryListener != null) {
                    tryListener.tryAgain ();
                }
            }
        });
    }

    public void setLoading(String loadingText) {
        tvLoadingText.setText (loadingText);
    }

    public void setEmpty(@DrawableRes int emptyIcon, String emptyText) {
        ivEmptyIcon.setImageResource (emptyIcon);
        tvEmptyText.setText (emptyText);
    }
    public void setError(@DrawableRes int errorIcon, String errorText){
        ivErrorIcon.setImageResource (errorIcon);
        tvErrorText.setText (errorText);
    }

    public void showContent() {
        statusLoading.setVisibility (GONE);
        statusEmpty.setVisibility (GONE);
        statusError.setVisibility (GONE);
        if (contentView != null) {
            contentView.setVisibility (VISIBLE);
        }
    }

    public void showError() {
        statusLoading.setVisibility (GONE);
        statusEmpty.setVisibility (GONE);
        statusError.setVisibility (VISIBLE);
        if (contentView != null) {
            contentView.setVisibility (GONE);
        }
    }

    public void showError(int errorIcon, String errorMsg, int backgroundColor) {
        setError(errorIcon,errorMsg);
        statusError.setBackgroundColor(backgroundColor);
        showError();
    }

    public void showLoading() {
        statusLoading.setVisibility (VISIBLE);
        statusEmpty.setVisibility (GONE);
        statusError.setVisibility (GONE);
        if (contentView != null) {
            contentView.setVisibility (GONE);
        }
    }

    public void dismissAll(){
        statusLoading.setVisibility (GONE);
        statusEmpty.setVisibility (GONE);
        statusError.setVisibility (GONE);
        if (contentView != null) {
            contentView.setVisibility (GONE);
        }
    }

    public void showEmpty() {
        statusLoading.setVisibility (GONE);
        statusEmpty.setVisibility (VISIBLE);
        statusError.setVisibility (GONE);
        if (contentView != null) {
            contentView.setVisibility (GONE);
        }
    }

    public void showEmpty(int iconID, String msg, int backgroundColor) {
        setEmpty(iconID,msg);
        statusEmpty.setBackgroundColor(backgroundColor);
        showEmpty();
    }

    public StatusView(@NonNull Context context) {
        this (context, null);
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public StatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        LayoutInflater.from (context).inflate (R.layout.ewallet_pay_status_view, this, true);
        assignViews ();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure (widthMeasureSpec, heightMeasureSpec);
        findContentView ();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout (changed, left, top, right, bottom);

    }

    void findContentView() {
        if (contentView == null) {
            int count = getChildCount ();
            for (int i = 0; i < count; i++) {
                View child = getChildAt (i);
                if (child.getId () != R.id.status_root) {
                    contentView = child;
                    break;
                }
            }
            showContent ();
        }
    }
}
