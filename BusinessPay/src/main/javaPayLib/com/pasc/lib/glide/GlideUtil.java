package com.pasc.lib.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.pasc.lib.glide.request.RequestOptions;
import com.pasc.lib.glide.request.target.SimpleTarget;
import com.pasc.lib.glide.request.transition.Transition;


/**
 * @author yangzijian
 * @date 2018/12/3
 * @des
 * @modify
 **/
public class GlideUtil {

    private static boolean isSafe(Context context, View view) {
        if (context == null || view == null) {
            return false;
        }
        if (context instanceof Activity) {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !((Activity) context).isDestroyed ();
        }
        return true;

    }

    public static void loadImage(Context context, ImageView imageView, String url, int placeRes, int errorRes) {
        if (!isSafe (context, imageView)) {
            return;
        }

        Glide.with (context).load (url)
                .apply (new RequestOptions ().error (placeRes)
                        .placeholder (errorRes)
                )
                .into (imageView);
    }

    public static void loadBg(Context context, View view, String url, int placeRes, int errorRes) {
        if (!isSafe (context, view)) {
            return;
        }

        Glide.with (context)
                .asDrawable ()
                .load (url).apply (new RequestOptions ().error (placeRes)
                .placeholder (errorRes)).into (new SimpleTarget<Drawable> () {

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setBackground (placeholder);
                } else {
                    view.setBackgroundDrawable (placeholder);
                }
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setBackground (resource);
                } else {
                    view.setBackgroundDrawable (resource);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setBackground (errorDrawable);
                } else {
                    view.setBackgroundDrawable (errorDrawable);
                }
            }
        });
    }
}
