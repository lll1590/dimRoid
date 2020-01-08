package com.dim.ke.framework.core.util.third;

import android.graphics.Bitmap;

public interface ImageLoadCallback {
    void onSuccess(Bitmap bitmap);
    default void onFailure(String reason){};
}
