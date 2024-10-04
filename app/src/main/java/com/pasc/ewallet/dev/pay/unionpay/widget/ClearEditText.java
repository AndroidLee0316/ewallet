package com.pasc.ewallet.dev.pay.unionpay.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import com.pasc.business.ewallet.R;

public class ClearEditText extends android.support.v7.widget.AppCompatEditText
    implements View.OnFocusChangeListener, TextWatcher {
  /**
   * 删除按钮的引用
   */
  public Drawable mClearDrawable;
  private Context context;
  private EditTextChangeListener editTextChangeListener;
  private int focusCount = 0;//焦点计数，大于1则表示重新获得焦点
  private boolean isNeedResetText = false;//是否需要重置

  /**
   * 控件是否有焦点
   */
  private boolean hasFocus;
  private int inputType;//输入类型
  private IconDismissListener iconDismissListener;
  private InnerFocusChangeListener innerFocusChangeListener = InnerFocusChangeListener.NONE;

  public interface InnerFocusChangeListener {
    InnerFocusChangeListener NONE = new InnerFocusChangeListener() {
      @Override public void onInnerFocusChange(View v, boolean hasFocus) {
      }
    };

    public void onInnerFocusChange(View v, boolean hasFocus);
  }

  public ClearEditText(Context context) {
    this(context, null);
  }

  public ClearEditText(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.editTextStyle);
  }
  public void setInnerFocusChangeListener(InnerFocusChangeListener innerFocusChangeListener){
    this.innerFocusChangeListener=innerFocusChangeListener;
  }
  public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
    mClearDrawable = getCompoundDrawables()[2];
    if (mClearDrawable == null) {
      mClearDrawable = getResources().getDrawable(R.drawable.temp_clear_edit_content);
    }
    mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
        mClearDrawable.getIntrinsicHeight());
    //默认设置隐藏图标
    setClearIconVisible(false);
    //设置焦点改变的监听
    setOnFocusChangeListener(this);
    //设置输入框里面内容发生改变的监听
    addTextChangedListener(this);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {

    if (mClearDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
      //            int x = (int) event.getX();
      //            //判断触摸点是否在水平范围内
      //            boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) && (x < (getWidth()
      //                    - getPaddingRight()));
      //            //获取删除图标的边界，返回一个Rect对象
      //            Rect rect = mClearDrawable.getBounds();
      //            //获取删除图标的高度
      //            int height = rect.height();
      //            int y = (int) event.getY();
      //            //计算图标底部到控件底部的距离
      //            int distance = (getHeight() - height) / 2;
      //            //判断触摸点是否在竖直范围内(可能会有点误差)
      //            //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
      //            boolean isInnerHeight = (y > distance) && (y < (distance + height));
      //            if (isInnerHeight && isInnerWidth) {
      //                this.setText("");
      //                if (iconDismissListener != null) {
      //                    iconDismissListener.onIconClick();
      //                }
      //            }

      int eventX = (int) event.getRawX();
      int eventY = (int) event.getRawY();
      Rect rect = new Rect();
      getGlobalVisibleRect(rect);
      rect.left = rect.right - 100;
      if (eventX<=rect.right&&eventX>=rect.left) {
        this.setText("");
        if (iconDismissListener != null) {
          iconDismissListener.onIconClick();
        }
      }
    }
    return super.onTouchEvent(event);
  }

  /**
   * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
   */
  private void setClearIconVisible(boolean visible) {
    Drawable right = visible ? mClearDrawable : null;
    setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right,
        getCompoundDrawables()[3]);
  }

  /**
   * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
   */
  @Override public void onFocusChange(View v, boolean hasFocus) {
    this.hasFocus = hasFocus;
    if (hasFocus) {
      setClearIconVisible(getText().length() > 0);
      focusCount++;
      inputType = getInputType();
    } else {
      if (TextUtils.isEmpty(getText())) {
        focusCount = 0;
      }
      setClearIconVisible(false);
    }
    innerFocusChangeListener.onInnerFocusChange(v,hasFocus);
  }

  /**
   * 1.当输入框里面内容发生变化的时候回调的方法
   * 2.当输入的手机号符合要求就回调该方法
   */
  @Override public void onTextChanged(CharSequence text, int start, int lengthBefore,
      int lengthAfter) {
    if (hasFocus) {
      setClearIconVisible(text.length() > 0);
    }
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    if (focusCount > 1 && isPasswordInputType() && count > 0) {//重新获得焦点，输入类型为密码，点删除按钮
      focusCount = 1;
      setText("");
    }
  }

  @Override public void afterTextChanged(Editable s) {
    if (editTextChangeListener != null) {
      editTextChangeListener.afterChange(s.toString());
    }
  }

  /**
   * 设置晃动动画
   */
  public void setShakeAnimation() {
    this.startAnimation(shakeAnimation(5));
  }

  /**
   * 晃动动画
   *
   * @param counts 1秒钟晃动多少下
   */
  public static Animation shakeAnimation(int counts) {
    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
    translateAnimation.setInterpolator(new CycleInterpolator(counts));
    translateAnimation.setDuration(1000);
    return translateAnimation;
  }

  public void setEditTextChangeListener(EditTextChangeListener editTextChangeListener) {
    this.editTextChangeListener = editTextChangeListener;
  }

  /**
   * 设置右边图标的点击事件
   */
  public void setIconDismissListener(IconDismissListener iconDismissListener) {
    this.iconDismissListener = iconDismissListener;
  }

  public interface IconDismissListener {
    void onIconClick();
  }

  public interface EditTextChangeListener {
    void afterChange(String s);
  }

  /**
   * 输入框输入类型是否为password
   */
  private boolean isPasswordInputType() {
    return inputType == EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD
        || inputType == (EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        | InputType.TYPE_CLASS_TEXT)
        || inputType == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        || inputType == EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD;
  }
}