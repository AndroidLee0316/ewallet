package com.pasc.business.ewallet.picture.pictureSelect;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pasc.business.ewallet.R;
import com.pasc.business.ewallet.picture.takephoto.app.TakePhoto;
import com.pasc.business.ewallet.picture.takephoto.app.TakePhotoFragmentActivity;
import com.pasc.business.ewallet.picture.takephoto.compress.CompressConfig;
import com.pasc.business.ewallet.picture.takephoto.model.CropOptions;
import com.pasc.business.ewallet.picture.takephoto.model.LubanOptions;
import com.pasc.business.ewallet.picture.takephoto.model.TResult;
import com.pasc.business.ewallet.picture.takephoto.model.TakePhotoOptions;
import com.pasc.business.ewallet.picture.takephoto.uitl.TConstant;
import com.pasc.business.ewallet.picture.util.AndroidPUtil;
import com.pasc.business.ewallet.picture.util.BitmapUtils;
import com.pasc.business.ewallet.picture.util.DensityUtils;
import com.pasc.business.ewallet.picture.util.FileUtils;
import com.pasc.business.ewallet.picture.util.StatusBarUtils;
import com.pasc.business.ewallet.widget.dialognt.CommonDialog;
import com.pasc.business.ewallet.picture.pictureSelect.activity.AlbumsActivity;

import org.reactivestreams.Publisher;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class NewPictureSelectActivity extends TakePhotoFragmentActivity implements View.OnClickListener {


    public static final int REQUEST_CODE_PICTURE_SELECT = 100;
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
    private boolean isCompress;

    private ArrayList<LocalPicture> allLocalPictures = new ArrayList<>();
    private ArrayList<LocalPicture> selectPictures = new ArrayList<>();
    private LocalPictureAdapter adapter;
    private BrowsePicturesAdapter browseAdapter;
    private ImageView ivBack;
    private TextView tvAlumn;
    private static boolean isHeadImg;

    public static void setIsHeadImg(boolean isheadImg) {
        isHeadImg = isheadImg;
    }


    public static void actionStart(Activity context, int requestCode, int canSelect) {
        Intent intent = new Intent(context, NewPictureSelectActivity.class);
        intent.putExtra(TConstant.INTENT_EXTRA_LIMIT, canSelect);
        context.startActivityForResult(intent, requestCode);
    }

    public static void actionStart(Activity context, int requestCode, int canSelect, boolean compress) {
        Intent intent = new Intent(context, NewPictureSelectActivity.class);
        intent.putExtra(TConstant.INTENT_EXTRA_LIMIT, canSelect);
        intent.putExtra(TConstant.INTENT_EXTRA_COMPRESS, compress);
        context.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ewallet_picture_activity_new_picture_select);
        tvSelect = findViewById(R.id.tv_select);
        recyclerView = findViewById(R.id.recycler_view);
        tvBrowse = findViewById(R.id.tv_browse);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvTitleRight = findViewById(R.id.tv_title_right);
        tvPicturePosition = findViewById(R.id.tv_picture_position);
        ll_title = findViewById(R.id.ll_title);
        viewpager = findViewById(R.id.viewpager);
        ivBack = findViewById(R.id.iv_title_left);
        tvAlumn = findViewById(R.id.tv_title_left);
        RelativeLayout rlBottom = findViewById(R.id.rl_bottom);
        rlBottom.setVisibility(View.GONE);
        initEvent();
        Intent in = getIntent();
        if (in != null) {
            canSelect = in.getIntExtra(TConstant.INTENT_EXTRA_LIMIT, deafultNum);
            isCompress = in.getBooleanExtra(TConstant.INTENT_EXTRA_COMPRESS, false);
            rlBottom.setVisibility(isHeadImg ? View.GONE : View.VISIBLE);
        }
        if (ImagePicker.isEnable()) {
            canSelect = ImagePicker.getSelectLimit();
        }

        initView();
        initData();
        clickBack();
        if (Build.VERSION.SDK_INT >= 28) {
            ll_title.post(new Runnable() {
                @Override
                public void run() {
                    int paddingTop = AndroidPUtil.getPaddingTop(NewPictureSelectActivity.this, DensityUtils.dip2px(NewPictureSelectActivity.this, 20));
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
                if (isHeadImg) {
                    StatusBarUtils.setStatusBarLightMode(NewPictureSelectActivity.this, true, false);
                    ll_title.setBackgroundColor(Color.parseColor("#bf000000"));
                    itemSelectorNot(position);
                    tvTitleRight.setVisibility(View.GONE);
                    tvTitleName.setVisibility(View.GONE);
                    ivBack.setVisibility(View.VISIBLE);
                    tvAlumn.setVisibility(View.GONE);
                    tvPicturePosition.setSelected(allLocalPictures.get(position).isSelect());
                    tvPicturePosition.setText(getSelectPosition(allLocalPictures.get(position)));

                    setResultData();
                } else {
                    if (i == R.id.fl_icon) {
                        itemSelectorNot(position);

                    } else if (i == R.id.img_local) {
                        StatusBarUtils.setStatusBarLightMode(NewPictureSelectActivity.this, true, false);
                        ll_title.setBackgroundColor(Color.parseColor("#bf000000"));
                        itemSelectorNot(position);
                        tvTitleRight.setVisibility(View.GONE);
                        tvTitleName.setVisibility(View.GONE);
                        tvPicturePosition.setVisibility(View.VISIBLE);
                        viewpager.setVisibility(View.VISIBLE);
                        ivBack.setVisibility(View.VISIBLE);
                        tvAlumn.setVisibility(View.GONE);
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

    public void showErrorDialog() {
        final CommonDialog commonDialog = new CommonDialog(this);
        commonDialog.setContent("最多只能选择" + canSelect + "张照片").setButton1("我知道了", CommonDialog.Blue_3B68E9).show();
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.setOnButtonClickListener(new CommonDialog.OnButtonClickListener() {
            @Override
            public void button1Click() {
                commonDialog.dismiss();
            }


        });
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void initData() {
        setSelectedSize(0);
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath()));
        files.add(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .getAbsolutePath()));
        files.add(new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath()));
        files.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/tencent/MicroMsg/WeiXin/"));
        files.add(new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/news_aticle"));
        files.add(new File(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/sina/weibo/"));
        Disposable disposable = Flowable.fromArray(files)
                .flatMap(new Function<ArrayList<File>, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(ArrayList<File> files) {
                        return Flowable.fromIterable(files);
                    }
                })
                .flatMap(new Function<File, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(File file) {
                        return listFiles(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        allLocalPictures.add(new LocalPicture(file.getPath()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        adapter.notifyDataChanged(selectPictures.size(), selectPictures);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private Publisher<File> listFiles(final File f) {
        if (f.isDirectory()) {
            return Flowable.fromArray(f.listFiles()).flatMap(new Function<File, Publisher<File>>() {
                @Override
                public Publisher<File> apply(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Flowable.just(f).filter(new Predicate<File>() {
                @Override
                public boolean test(File file) {
                    return file.getName().endsWith(".jpeg")
                            || file.getName().endsWith(".JPEG")
                            || file.getName().endsWith(".jpg")
                            || file.getName().endsWith(".JPG")
                            || file.getName().endsWith(".png")
                            || file.getName().endsWith(".PNG");
                }
            });
        }
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        tvAlumn.setOnClickListener(this);
        findViewById(R.id.tv_title_right).setOnClickListener(this);
        findViewById(R.id.tv_picture_position).setOnClickListener(this);
        tvSelect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_title_left) {
            clickBack();

        } else if (i == R.id.tv_title_right) {
            selectPictures.clear();
            finish();

        } else if (i == R.id.tv_select) {
            setResultData();

        } else if (i == R.id.tv_picture_position) {
            itemSelectorNot(viewpager.getCurrentItem());

        } else if (i == R.id.tv_title_left) {
            chooseFromGallery();
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
        StatusBarUtils.setStatusBarLightMode(NewPictureSelectActivity.this, true, true);
        ll_title.setBackgroundColor(Color.WHITE);
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleName.setVisibility(View.VISIBLE);
        tvPicturePosition.setVisibility(View.GONE);
        viewpager.setVisibility(View.GONE);
        ivBack.setVisibility(View.GONE);
        tvAlumn.setVisibility(View.VISIBLE);

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

    //从相册选择
    private void chooseFromGallery() {
        TakePhoto cameraTakePhoto = getTakePhoto();
        configCompress(cameraTakePhoto);
        configTakePhotoOption(cameraTakePhoto);

//            getTakePhoto().onPickFromGallery();
        AlbumsActivity.actionStart(this, REQUEST_CODE_PICTURE_SELECT, canSelect,isHeadImg);

    }


    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //是否使用TakePhoto自带相册
        builder.setWithOwnGallery(false);
        //纠正拍照的照片旋转角度
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void configCompress(TakePhoto takePhoto) {
       /* if (!ImagePicker.getInstance().isEnableCompress()) {
            takePhoto.onEnableCompress(null, false);
            return;
        }*/
        int maxSize = 102400; // 最大 100kb
        int width = 800;
        int height = 800;
        /***是否显示进度**/
        boolean showProgressBar = true;
        /***压缩后是否保留原图***/
        boolean enableRawFile = true;
        CompressConfig config;
        if (true /****android 原生压缩***/) {
            config = new CompressConfig.Builder().setMaxSize(maxSize)
                    .setMaxPixel(width >= height ? width : height)
                    .enableReserveRaw(enableRawFile)
                    .create();
        } else {
            LubanOptions option = new LubanOptions.Builder().setMaxHeight(height).setMaxWidth(width).setMaxSize(maxSize).create();
            config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }
        takePhoto.onEnableCompress(config, showProgressBar);

    }

    //裁剪图片属性
    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(800).setAspectY(800);//裁剪时的尺寸比例
        //builder.setOutputX(800).setOutputY(800);
        builder.setWithOwnCrop(false);//s使用第三方还是takephoto自带的裁剪工具
        return builder.create();
    }

    @Override
    public void takeSuccess(TResult result) {

        Log.e("takeSuccess", result.getImages().toString());
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setImageURI(Uri.fromFile(new File(result.getImage().getCompressPath())));
        selectPictures.clear();
        selectPictures.add(new LocalPicture(result.getImages().get(0).getCompressPath()));
        setResultData();

    }

    private void setResultData() {
        if (selectPictures != null && selectPictures.size() > 0) {
            ArrayList<String> images = new ArrayList<>();
            for (int index = 0; index < selectPictures.size(); index++) {
                images.add(selectPictures.get(index).path);
            }
            Intent intent = new Intent();
            if (ImagePicker.getInstance().isEnableCompress() || isCompress) {
                intent.putStringArrayListExtra(TConstant.INTENT_EXTRA_IMAGES, getCompressPicturePaths(images));
            } else {
                intent.putStringArrayListExtra(TConstant.INTENT_EXTRA_IMAGES, images);
            }
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.e("takeFail", result.getImages().toString());

    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        Log.e("takeCancel", "cancel");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICTURE_SELECT && data != null) {
            ArrayList<String> pictures = data.getStringArrayListExtra("images");
            Intent intent = new Intent();
            if (ImagePicker.getInstance().isEnableCompress() || isCompress) {
                intent.putStringArrayListExtra(TConstant.INTENT_EXTRA_IMAGES, getCompressPicturePaths(pictures));
            } else {
                intent.putStringArrayListExtra(TConstant.INTENT_EXTRA_IMAGES, pictures);
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * 压缩图片
     *
     * @param pictures
     * @return
     */
    private ArrayList<String> getCompressPicturePaths(ArrayList<String> pictures) {
        ArrayList<String> destPicturePaths = new ArrayList<>();
        for (String picturePath : pictures) {
            String[] split = picturePath.split("/");
            String destPicturePath =
                    FileUtils.getImgFolderPath() + split[split.length - 1];
            BitmapUtils.compressPhoto(picturePath, destPicturePath, 300, 1);
            destPicturePaths.add(destPicturePath);
        }
        return destPicturePaths;
    }
}
