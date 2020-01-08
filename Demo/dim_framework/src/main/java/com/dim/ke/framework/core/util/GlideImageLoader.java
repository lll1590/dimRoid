package com.dim.ke.framework.core.util;

import android.content.Context;
import android.widget.ImageView;

import com.dim.ke.framework.core.util.third.GlideUtils;
import com.lcw.library.imagepicker.utils.ImageLoader;

//imagepicker的glide图片加载器
public class GlideImageLoader implements ImageLoader {
    Context mContext;
    public GlideImageLoader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void loadImage(ImageView imageView, String imagePath) {
        String imgUrl = GlideUtils.IMAGE_SD_HEADER + imagePath;
        GlideUtils.displayImage(imgUrl, imageView);
    }

    @Override
    public void loadPreImage(ImageView imageView, String imagePath) {
        String imgUrl = GlideUtils.IMAGE_SD_HEADER + imagePath;
        GlideUtils.displayImage(imgUrl, imageView);
    }

    @Override
    public void clearMemoryCache() {
        GlideUtils.clearMomeryCache(mContext);
    }
}
