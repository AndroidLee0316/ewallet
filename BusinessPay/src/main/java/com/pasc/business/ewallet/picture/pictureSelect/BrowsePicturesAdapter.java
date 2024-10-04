package com.pasc.business.ewallet.picture.pictureSelect;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pasc.business.ewallet.R;
import com.pasc.lib.glide.GlideUtil;

import java.util.List;


public class BrowsePicturesAdapter extends PagerAdapter {

    private final List<LocalPicture> localPictures;
    private final Context context;
    private int mChildCount = 0;

    public BrowsePicturesAdapter(Context context, List<LocalPicture> localPictures) {
        this.localPictures = localPictures;
        this.context = context;
    }

    @Override
    public int getCount() {
        return localPictures != null ? localPictures.size () : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView (context);
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams (params);
        imageView.setBackgroundColor (Color.WHITE);
        GlideUtil.loadImage (context,imageView,
                localPictures.get (position).path,
                R.drawable.ewallet_bg_default_image_color, R.drawable.ewallet_bg_default_image_color);

        container.addView (imageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView ((View) object);
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount ();
        super.notifyDataSetChanged ();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition (object);
    }
}
