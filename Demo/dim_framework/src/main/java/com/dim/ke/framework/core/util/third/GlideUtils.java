package com.dim.ke.framework.core.util.third;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dim.ke.framework.R;
import com.dim.ke.framework.core.util.LogUtils;

public class GlideUtils {

    public final static String IMAGE_NET_HEADER = "http://";
    public final static String IMAGE_SD_HEADER = "file://";
    public final static String IMAGE_MEDIA_HEADER = "content://";
    public final static String IMAGE_ASSET_HEADER = "assets://";
    public final static String IMAGE_RES_HEADER = "drawable://";

    public static void displayDrawableImage(final int resId, ImageView imageView) {
        Glide.with(imageView).asDrawable().load(resId).into(imageView);
    }

    public static void simpleDisplay(String uri,ImageView imageView){
        Glide.with(imageView.getContext()).load(uri).into(imageView);
    }

    public static void displayImage(final String uri, ImageView imageView) {
        displayImage(uri, imageView, -1);
    }

    public static void displayImage(final String uri, ImageView imageView, int resId) {
        displayImage(uri, imageView, resId, null);
    }

    public static void displayImage(final String uri, ImageView imageView, int resId, RequestOptions displayOption) {
        if (displayOption == null) {
            displayOption = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .error(resId == -1 ? R.drawable.glide_default_drawable : resId)
                    .placeholder(resId == -1 ? R.drawable.glide_default_drawable : resId);
        }
        Glide.with(imageView).asDrawable().load(uri).apply(displayOption).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (e != null) {
                    LogUtils.e("GlideUtils", "url:" + uri + " ex:" + e.getMessage());
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }

    public static void dispalyGifImage(final String uri, ImageView imageView) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        Glide.with(imageView).asGif().load(uri).into(imageView);
    }

    public static void dispalyGifImage(final int resId, ImageView imageView) {
        Glide.with(imageView).asGif().load(resId).into(imageView);
    }

    public static void displayVideoFrame(final String uri, ImageView imageView) {
        displayVideoFrame(uri, imageView, 1);
    }

    public static void displayVideoFrame(final String uri, ImageView imageView, int second) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        Glide.with(imageView).asDrawable().apply(new RequestOptions().frame(second * 1000000).centerCrop()).load(uri).into(imageView);
    }

    public static void displayDrawable(final String uri, final View view) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        Glide.with(view).asDrawable().load(uri).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                view.setBackground(resource);
                return false;
            }
        }).submit();
    }

    public static void getBitmapFromUrl(Context context, final String uri, final ImageLoadCallback imageLoadListener) {
        if (TextUtils.isEmpty(uri)) {
            if (imageLoadListener != null) {
                imageLoadListener.onFailure("empty url");
            }
            return;
        }
        Glide.with(context).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                if (imageLoadListener != null) {
                    imageLoadListener.onFailure("cause " + (e == null ? "null" : e.getMessage()));
                }
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                LogUtils.d("imageUri:" + uri + " loaded success!");
                if (imageLoadListener != null) {
                    imageLoadListener.onSuccess(resource);
                }
                return true;
            }
        }).submit();
    }

    //清除bitmapPool内存缓存
    public static void clearBitmapPool(Context context) {
        Glide.get(context).getBitmapPool().clearMemory();
    }

    //    清除所有内存缓存(需要在Ui线程操作)
    public static void clearMomeryCache(Context context) {

        Glide.get(context.getApplicationContext()).clearMemory();
    }

    //清除所有磁盘缓存(需要在子线程操作)
    public static void clearDiskCache(final Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context).clearDiskCache();
                }
            }).start();
        } else {
            Glide.get(context).clearDiskCache();
        }
    }

    public static void onResume(Context context) {
        Glide.with(context).onStart();
    }

    public static void onPause(Context context) {
        Glide.with(context).onStop();
    }
}
