package com.pasc.business.ewallet.widget.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pasc.business.ewallet.NotProguard;
import com.pasc.business.ewallet.widget.DensityUtils;
import com.pasc.business.ewallet.R;
import com.pasc.lib.pay.common.util.CheckBrandUtils;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@NotProguard
public class PascToolbar extends FrameLayout {

  private TextView titleView;
  private TextView subTitleView;
  private LinearLayout leftContainer;
  private LinearLayout titleContainer;
  private LinearLayout rightContainer;
  private ImageView underDivider;

  private int menuPaddingStart;
  private int menuPaddingEnd;
  private int menuLeftPadding;
  private int menuRightPadding;
  private CharSequence titleText;
  private int titleColor;
  private int titleSize;
  private CharSequence subTitleText;
  private int subTitleColor;
  private int subTitleSize;
  private int menuTextSize;// sp
  private int menuTextColor;
  private int backIconRes;
  private int underDividerHeight;
  private int underDividerColor;
    private boolean supportTranslucentStatusBar;
  private int toolbarHeight;

  public static final int RIGHT_BUTTOM_ID = 1000;

    public PascToolbar(Context context) {
    this(context, null);
  }

  public PascToolbar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PascToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray array =
            getContext().obtainStyledAttributes(attrs, R.styleable.EwalletPascToolbar, defStyleAttr, 0);

    toolbarHeight = array.getDimensionPixelSize(R.styleable.EwalletPascToolbar_android_height, DensityUtils.dp2px(44));
    if (toolbarHeight == ViewGroup.LayoutParams.MATCH_PARENT
            || toolbarHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
      throw new IllegalArgumentException("高度必须为一个指定的值，不能为 MATCH_PARENT 或者 WRAP_CONTENT ");
    }

    titleSize =
            array.getDimensionPixelSize(R.styleable.EwalletPascToolbar_ewallet_title_text_size,DensityUtils.dp2px(18));
    titleColor =
            array.getColor(R.styleable.EwalletPascToolbar_ewallet_title_text_color, Color.parseColor("#333333"));
    subTitleSize = array.getDimensionPixelSize(R.styleable.EwalletPascToolbar_ewallet_sub_title_text_size,
            DensityUtils.dp2px(13));
    subTitleColor = array.getColor(R.styleable.EwalletPascToolbar_ewallet_sub_title_text_color,
            Color.parseColor("#333333"));

    final CharSequence title = array.getText(R.styleable.EwalletPascToolbar_ewallet_title);
    if (!TextUtils.isEmpty(title)) {
      setTitle(title);
    }

    final CharSequence subTitle = array.getText(R.styleable.EwalletPascToolbar_ewallet_sub_title);
    if (!TextUtils.isEmpty(subTitle)) {
      setSubTitle(subTitle);
    }

    backIconRes = array.getResourceId(R.styleable.EwalletPascToolbar_ewallet_back_icon, R.drawable.ewallet_back_icon);

    menuRightPadding = DensityUtils.dp2px(15);
    menuLeftPadding = DensityUtils.dp2px(15);
    menuPaddingStart = menuPaddingEnd = DensityUtils.dp2px(15);
    menuTextSize =
            array.getDimensionPixelSize(R.styleable.EwalletPascToolbar_ewallet_menu_text_size, DensityUtils.dp2px(15));
    menuTextColor =
            array.getColor(R.styleable.EwalletPascToolbar_ewallet_menu_text_color, Color.parseColor("#333333"));

      boolean enableUnderDivider = array.getBoolean(R.styleable.EwalletPascToolbar_ewallet_enable_under_divider, true);
    underDividerColor =
            array.getColor(R.styleable.EwalletPascToolbar_ewallet_under_divider_color, Color.parseColor("#E0E0E0"));
    underDividerHeight =
            array.getDimensionPixelSize(R.styleable.EwalletPascToolbar_ewallet_under_divider_height, 1); //默认1px
    if (enableUnderDivider) {
      enableUnderDivider(true);
    }

    supportTranslucentStatusBar = array.getBoolean(R.styleable.EwalletPascToolbar_ewallet_support_translucent_status_bar, false);

    array.recycle();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = heightMeasureSpec;
    if(CheckBrandUtils.isMui5()){
        int topMargin = 22;
        int topMarginPx = DensityUtils.dp2px(topMargin);
      height = height + topMarginPx;
//      this.setPadding(getLeft() , getTop() + topMarginPx+30, getRight(),
//              0);
    }
    super.onMeasure(widthMeasureSpec, height);
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }

  public void enableUnderDivider(boolean enableUnderDivider) {
    if (underDivider == null) {
      underDivider = new ImageView(getContext());
      ViewGroup.LayoutParams lp = new LayoutParams(MATCH_PARENT, underDividerHeight);
      ((LayoutParams) lp).gravity = Gravity.BOTTOM;
      underDivider.setBackgroundColor(underDividerColor);
      addView(underDivider, lp);
    }
    underDivider.setVisibility(enableUnderDivider ? VISIBLE : GONE);
  }

  public void setTitle(CharSequence title) {
    if (!TextUtils.isEmpty(title)) {
      if (titleView == null) {
        titleView = new AppCompatTextView(getContext());
        titleView.setSingleLine();
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        titleView.setTextColor(titleColor);
        titleView.setMaxEms(13);
        titleView.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        makeSureTitleContainer();
        titleContainer.addView(titleView, lp);
      }
      titleView.setText(title);
    }
    this.titleText = title;
  }

  /**
   * 更新左边image的图标
   * @param drawableID
   */
  public void updateLeftImageResID(int drawableID){
      if (leftContainer != null){
        for (int i = 0; i < leftContainer.getChildCount(); i++){
          if (leftContainer.getChildAt(i) instanceof ImageView){
            ((ImageView)leftContainer.getChildAt(i)).setImageResource(drawableID);
          }
        }
      }
  }

  /**
   * 确保标题容器实例化
   * @return 返回菜单容器
   */
  private LinearLayout makeSureTitleContainer() {
    if (titleContainer == null) {
      titleContainer = new LinearLayout(getContext());
      titleContainer.setOrientation(LinearLayout.VERTICAL);
      titleContainer.setGravity(Gravity.CENTER_HORIZONTAL);
      LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
      lp.gravity = Gravity.CENTER;
      addView(titleContainer, lp);
    }
    return titleContainer;
  }

  /**
   * 确保左菜单容器实例化
   * @return 返回菜单容器
   */
  private LinearLayout makeSureLeftContainer() {
    if (leftContainer == null) {
      leftContainer = new LinearLayout(getContext());
      leftContainer.setOrientation(LinearLayout.HORIZONTAL);
      LayoutParams lp = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
      lp.gravity = Gravity.LEFT;
      addView(leftContainer, lp);
    }
    return leftContainer;
  }

  /**
   * 确保右菜单容器实例化
   * @return 返回菜单容器
   */
  private LinearLayout makeSureRightContainer() {
    if (rightContainer == null) {
      rightContainer = new LinearLayout(getContext());
      rightContainer.setOrientation(LinearLayout.HORIZONTAL);
      LayoutParams lp = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
      lp.gravity = Gravity.RIGHT;
      addView(rightContainer, lp);
    }
    return leftContainer;
  }

  public void setSubTitle(CharSequence subTitle) {
    if (!TextUtils.isEmpty(subTitle)) {
      if (subTitleView == null) {
        subTitleView = new AppCompatTextView(getContext());
        subTitleView.setSingleLine();
        subTitleView.setEllipsize(TextUtils.TruncateAt.END);
        subTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTitleSize);
        subTitleView.setTextColor(subTitleColor);
        subTitleView.setMaxEms(13);
        subTitleView.setGravity(Gravity.CENTER);
        subTitleView.setText(subTitle);
        LayoutParams lp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        makeSureTitleContainer();
        titleContainer.addView(subTitleView, lp);
      }
      subTitleView.setText(subTitle);
    }
    subTitleText = subTitle;
  }

  @Nullable public CharSequence getSubTitle() {
    return subTitleText;
  }

  @Nullable public CharSequence getTitle() {
    return titleText;
  }

  /**
   * 获取标题的view，可能返回空，如果没有设置子标题的话
   * @return titleView
   */
  @Nullable public TextView getTitleView() {
    return titleView;
  }

  /**
   * 获取副标题的view，可能返回空，如果没有设置子标题的话
   * @return subTitleView
   */
  @Nullable public TextView getSubTitleView() {
    return subTitleView;
  }

  /**
   * 获取放置标题的容器，可能返回空，如果没有设置过标题的话
   * @return
   */
  @Nullable public LinearLayout getTitleContainer(){
    return titleContainer;
  }

  public void clearLeftMenu() {
    this.leftContainer.removeAllViews();
  }

  public void clearRightMenu() {
    this.rightContainer.removeAllViews();
  }

  /**
   * 添加关闭/返回按钮
   * @return
   */
  public ImageButton addCloseImageButton() {
    return addLeftImageButton(backIconRes);
  }

  public ImageButton addLeftImageButton(@DrawableRes int drawableResId) {
    makeSureLeftContainer();
    final ImageButton imageButton = generateImageButton();
    imageButton.setPadding(leftContainer.getChildCount() == 0 ? menuPaddingStart : menuLeftPadding,
            0, menuLeftPadding, 0);
    imageButton.setImageResource(drawableResId);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
    imageButton.setLayoutParams(lp);
    leftContainer.addView(imageButton);
    return imageButton;
  }


  public TextView addLeftTextButton(String text) {
    makeSureLeftContainer();
    final TextView textButton = generateTextButton();
    textButton.setText(text);
    textButton.setPadding(leftContainer.getChildCount() == 0 ? menuPaddingStart : menuLeftPadding,
            0, menuLeftPadding, 0);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
    textButton.setLayoutParams(lp);
    leftContainer.addView(textButton);
    return textButton;
  }

  public ImageButton addRightImageButton(@DrawableRes int drawableResId) {
    makeSureRightContainer();
    final ImageButton imageButton = generateImageButton();
    imageButton.setImageResource(drawableResId);
    imageButton.setPadding(menuRightPadding, 0,
            rightContainer.getChildCount() == 0 ? menuPaddingEnd : menuRightPadding, 0);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
    imageButton.setLayoutParams(lp);
    rightContainer.addView(imageButton, 0);
    return imageButton;
  }

  public TextView addRightTextButton(String text) {
    makeSureRightContainer();
    final TextView textButton = generateTextButton();
    textButton.setId(RIGHT_BUTTOM_ID);
    textButton.setText(text);
    textButton.setPadding(menuRightPadding, 0,
            rightContainer.getChildCount() == 0 ? menuPaddingEnd : menuRightPadding, 0);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
    textButton.setLayoutParams(lp);
    rightContainer.addView(textButton, 0);
    return textButton;
  }

  public TextView addRightTextButton(String text,int textColor,int bgID) {
    makeSureRightContainer();
    final TextView textButton = generateTextButton();
    textButton.setId(RIGHT_BUTTOM_ID);
    textButton.setText(text);
    textButton.setPadding(menuRightPadding, menuRightPadding/2,
            rightContainer.getChildCount() == 0 ? menuRightPadding : menuRightPadding, menuRightPadding/2);
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    lp.gravity = Gravity.CENTER_VERTICAL;
    lp.rightMargin = menuRightPadding;
    textButton.setLayoutParams(lp);
    textButton.setTextColor(textColor);
    textButton.setBackgroundResource(bgID);
    rightContainer.addView(textButton, 0);

    return textButton;
  }

  public void updateRightButtonBG(int textColor, int bgID){
    TextView view = rightContainer.findViewById(RIGHT_BUTTOM_ID);
    view.setTextColor(textColor);
    view.setBackgroundResource(bgID);
  }

  private TextView generateTextButton() {
    final TextView textButton = new TextView(getContext());
    textButton.setGravity(Gravity.CENTER);
    textButton.setClickable(true);
    textButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
    textButton.setTextColor(menuTextColor);
    textButton.setBackgroundResource(R.drawable.ewallet_widget_selector_button_common);
    return textButton;
  }

  private ImageButton generateImageButton() {
    final ImageButton imageButton = new ImageButton(getContext());
    imageButton.setBackgroundResource(R.drawable.ewallet_widget_selector_button_common);
    return imageButton;
  }

  @Override protected boolean fitSystemWindows(Rect insets) {
    if (supportTranslucentStatusBar && Build.VERSION.SDK_INT < 20) {
      ViewGroup.LayoutParams lp = this.getLayoutParams();
      lp.height = toolbarHeight + insets.top;
      this.setLayoutParams(lp);
      this.setPadding(insets.left + getPaddingLeft(), insets.top, insets.right + getPaddingRight(),
              0);
    }
//    if(!CheckBrandUtils.isMui5()){
//      ViewGroup.LayoutParams lp = this.getLayoutParams();
//      int topMarginPx = DensityUtils.dp2px(mTopMargin);
//      lp.height = toolbarHeight;
//      this.setLayoutParams(lp);
//      int top = insets.top;
//      this.setPadding(insets.left , insets.top +topMarginPx , insets.right,
//              0);
//    }
    return super.fitSystemWindows(insets);
  }

  @Override public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
    if (supportTranslucentStatusBar && Build.VERSION.SDK_INT >= 20) {
      ViewGroup.LayoutParams lp = this.getLayoutParams();
      lp.height = toolbarHeight + insets.getSystemWindowInsetTop();
      this.setLayoutParams(lp);
      this.setPadding(insets.getSystemWindowInsetLeft() + getPaddingLeft(),
              insets.getSystemWindowInsetTop(),
              insets.getSystemWindowInsetRight() + getPaddingRight(), 0);
    }
    return super.dispatchApplyWindowInsets(insets);
  }
}
