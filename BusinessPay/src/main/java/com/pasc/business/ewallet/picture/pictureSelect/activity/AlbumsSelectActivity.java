package com.pasc.business.ewallet.picture.pictureSelect.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.picture.pictureSelect.BrowsePicturesAdapter;
import com.pasc.business.ewallet.picture.pictureSelect.ImagePicker;
import com.pasc.business.ewallet.picture.pictureSelect.LocalPictureAdapter;
import com.pasc.business.ewallet.picture.takephoto.uitl.TConstant;
import com.pasc.business.ewallet.picture.util.AndroidPUtil;
import com.pasc.business.ewallet.picture.util.DensityUtils;
import com.pasc.business.ewallet.picture.util.StatusBarUtils;
import com.pasc.business.ewallet.widget.dialognt.CommonDialog;
import com.pasc.business.ewallet.picture.pictureSelect.LocalPicture;
import com.pasc.business.ewallet.picture.pictureSelect.bean.PhotoUpImageBucket;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

import static com.pasc.business.ewallet.picture.pictureSelect.activity.AlbumsActivity.IS_HEAD;

public class AlbumsSelectActivity extends AppCompatActivity implements View.OnClickListener {


    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView tvSelect;
    private RecyclerView recyclerView;
    private TextView tvBrowse;
    private TextView tvTitleName;
    private TextView tvTitleRight;
    private TextView tvPicturePosition;
    private View ll_title;
    private ViewPager viewpager;

    private static final int deafultNum = 1;
    private int canSelect = deafultNum;

    private ArrayList<LocalPicture> allLocalPictures = new ArrayList<>();
    private ArrayList<LocalPicture> selectPictures = new ArrayList<>();
    private LocalPictureAdapter adapter;
    private BrowsePicturesAdapter browseAdapter;
    private boolean isHead;


    public static void actionStart(Activity context, int requestCode, int canSelect, PhotoUpImageBucket photoUpImageBucket, boolean ishead) {
        Intent intent = new Intent(context, AlbumsSelectActivity.class);
        intent.putExtra(TConstant.INTENT_EXTRA_LIMIT, canSelect);
        intent.putExtra("imagelist", photoUpImageBucket);
        intent.putExtra(IS_HEAD, ishead);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ewallet_picture_activity_ablums_select);
        tvSelect = findViewById(R.id.tv_select);
        recyclerView = findViewById(R.id.recycler_view);
        tvBrowse = findViewById(R.id.tv_browse);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvTitleRight = findViewById(R.id.tv_title_right);
        tvPicturePosition = findViewById(R.id.tv_picture_position);
        ll_title = findViewById(R.id.ll_title);
        View rlBottom = findViewById(R.id.rl_bottom);
        viewpager = findViewById(R.id.viewpager);
        ImagePicker imagePicker = ImagePicker.getInstance();
        initEvent();
        Intent in = getIntent();
        if (in != null) {
            canSelect = in.getIntExtra(TConstant.INTENT_EXTRA_LIMIT, deafultNum);
            isHead = in.getBooleanExtra(IS_HEAD, false);
            rlBottom.setVisibility(isHead?View.GONE:View.VISIBLE);

        }
        if (ImagePicker.isEnable()) {
            canSelect = ImagePicker.getSelectLimit();
        }
        if (canSelect > 0) {
            canSelect = canSelect > 9 ? 9 : canSelect;
        }
        initView();
        initData();
        clickBack();
        if (Build.VERSION.SDK_INT >= 28) {
            ll_title.post(new Runnable() {
                @Override
                public void run() {
                    int paddingTop = AndroidPUtil.getPaddingTop(getActivity(), DensityUtils.dip2px(getActivity(), 20));
                    ll_title.setPadding(0, paddingTop, 0, 0);
                }
            });

        }
    }

    private void initView() {
        int spanCount = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new LocalPictureAdapter(this, spanCount, allLocalPictures, canSelect, selectPictures);
        recyclerView.setAdapter(adapter);
        browseAdapter = new BrowsePicturesAdapter(this, allLocalPictures);
        viewpager.setAdapter(browseAdapter);
        adapter.setItemClick(new LocalPictureAdapter.ItemClick() {
            @Override
            public void click(View view, int position) {
                int i = view.getId();
                if (isHead) {
                    StatusBarUtils.setStatusBarLightMode(AlbumsSelectActivity.this, true, false);
                    ll_title.setBackgroundColor(Color.parseColor("#bf000000"));
                    itemSelectorNot(position);
                    tvTitleRight.setVisibility(View.GONE);
                    tvTitleName.setVisibility(View.GONE);
                    tvPicturePosition.setSelected(allLocalPictures.get(position).isSelect());
                    tvPicturePosition.setText(getSelectPosition(allLocalPictures.get(position)));
                    setResult();
                } else {
                    if (i == R.id.fl_icon) {
                        itemSelectorNot(position);

                    } else if (i == R.id.img_local) {
                        StatusBarUtils.setStatusBarLightMode(getActivity(), true, false);
                        ll_title.setBackgroundColor(Color.parseColor("#bf000000"));

                        tvTitleRight.setVisibility(View.GONE);
                        tvTitleName.setVisibility(View.GONE);
                        tvPicturePosition.setVisibility(View.VISIBLE);
                        viewpager.setVisibility(View.VISIBLE);
                        tvPicturePosition.setSelected(allLocalPictures.get(position).isSelect());
                        tvPicturePosition.setText(
                                getSelectPosition(allLocalPictures.get(position)));
                        viewpager.setCurrentItem(position);
                        browseAdapter.notifyDataSetChanged();

                    }
                }


            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                tvPicturePosition.setSelected(allLocalPictures.get(position).isSelect());
                tvPicturePosition.setText(getSelectPosition(allLocalPictures.get(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setResult() {
        if (selectPictures != null && selectPictures.size() > 0) {
            ArrayList<String> images = new ArrayList<>();
            for (int index = 0; index < selectPictures.size(); index++) {
                images.add(selectPictures.get(index).path);
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(TConstant.INTENT_EXTRA_IMAGES, images);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private void showErrorDialog() {
        final CommonDialog commonDialog = new CommonDialog(this);
        commonDialog.setContent("最多只能选择" + canSelect + "张照片").setButton1("我知道了", CommonDialog.Blue_3B68E9).show();

        commonDialog.setOnButtonClickListener(new CommonDialog.OnButtonClickListener() {
            @Override
            public void button1Click() {
                commonDialog.dismiss();
            }


        });
    }

    private void initData() {
        setSelectedSize(0);
        PhotoUpImageBucket photoUpImageBucket = (PhotoUpImageBucket) getIntent().getSerializableExtra("imagelist");
        if (photoUpImageBucket != null && photoUpImageBucket.getImageList() != null) {
            allLocalPictures.addAll(photoUpImageBucket.getImageList());
            adapter.notifyDataChanged(selectPictures.size(), selectPictures);
        }


    }

    private void initEvent() {
        findViewById(R.id.iv_title_left).setOnClickListener(this);
        findViewById(R.id.tv_title_right).setOnClickListener(this);
        findViewById(R.id.tv_picture_position).setOnClickListener(this);
        tvSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            if (viewpager.getVisibility() == View.GONE) {
                finish();
                return;
            }
            clickBack();

        } else if (i == R.id.tv_title_right) {
            selectPictures.clear();
            finish();

        } else if (i == R.id.tv_select) {
            setResult();

        } else if (i == R.id.tv_picture_position) {
            itemSelectorNot(viewpager.getCurrentItem());

        }
    }

    private void itemSelectorNot(int position) {
        LocalPicture localPicture = allLocalPictures.get(position);
        if (selectPictures.size() >= canSelect && !selectPictures.contains(localPicture)) {
            showErrorDialog();
            return;
        }
        if (localPicture.isSelect()) {
            //取消勾选
            localPicture.setSelect(false);
            tvPicturePosition.setSelected(false);
            selectPictures.remove(localPicture);
        } else { //勾选
            localPicture.setSelect(true);
            tvPicturePosition.setSelected(true);
            selectPictures.add(localPicture);

        }

        tvPicturePosition.setText(getSelectPosition(allLocalPictures.get(position)));
        adapter.notifyDataChanged(selectPictures.size(), selectPictures);
        browseAdapter.notifyDataSetChanged();
        setSelectedSize(selectPictures.size());
    }

    private void setSelectedSize(int position) {
        String text = String.format("已选择(%s/" + canSelect + ")", position);
        int len = 4 + ("" + position).length();
        SpannableString msp = new SpannableString(text);
        int color = Color.parseColor("#4d73f4");
        msp.setSpan(new ForegroundColorSpan(color), 4, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBrowse.setText(msp);
    }

    private void clickBack() {
        ll_title.setBackgroundColor(Color.WHITE);
        StatusBarUtils.setStatusBarLightMode(getActivity(), true, true);
        ll_title.setBackgroundColor(Color.WHITE);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleName.setVisibility(View.VISIBLE);
        tvPicturePosition.setVisibility(View.GONE);
        viewpager.setVisibility(View.GONE);

    }

    private String getSelectPosition(LocalPicture picture) {

        for (int i = 0; i < selectPictures.size(); i++) {
            LocalPicture localPicture = selectPictures.get(i);
            if (localPicture.equals(picture)) {
                return String.valueOf(i + 1);
            }
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        if (viewpager.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            clickBack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    protected AppCompatActivity getActivity() {
        return this;
    }

}
