package com.dim.ke.framework.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class BitmapUtils {

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取bitmap的大小
     *
     * @param bitmap
     * @return
     */
    @SuppressLint("NewApi")
    public static long getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


    /**
     * 压缩图片至能上传的尺寸和质量（480*800、500k)
     *
     * @param srcPath
     * @return
     */
    public static Bitmap compressImageToUpload(String srcPath) {
        // 现在主流手机比较多是800*480分辨率
        Bitmap bmp1 = compressImageBySize(srcPath, 720f, 720f);
        Bitmap bmp2 = compressImageByQuality(bmp1, 150);
        return bmp2;
    }

    /**
     * 压缩图片到指定质量.
     *
     * @param image
     * @return
     */
    public static Bitmap compressImageByQuality(Bitmap image, int kbNum) {
        byte[] compressImageByQualityToByte = compressImageByQualityToByte(image, kbNum);
        ByteArrayInputStream isBm = new ByteArrayInputStream(compressImageByQualityToByte);
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100的话表示不压缩质量
        long length = baos.toByteArray().length / 1024;//读出图片的kb大小


        return bitmap;
    }


    /**
     * 压缩图片到指定质量的字节数组
     *
     * @param image
     * @param kbNum
     * @return
     */
    public static byte[] compressImageByQualityToByte(Bitmap image, int kbNum) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] byteArray = null;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > kbNum && options>0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 减少压缩比例变化梯度，更精确控制图片大小 TODO 需要改进获取最佳压缩比算法，减少多次尝试的开销
            if (options<=0){
                options=100;
            }
        }
        byteArray = baos.toByteArray();
        LogUtils.d("compressImageByQualityToByte",byteArray.length/1024+"kb");

        try {
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//            image.recycle();// 对原图进行回收
        return byteArray;
    }


    /**
     * 压缩图片到指定尺寸
     *
     * @param srcPath
     * @param width
     * @param height
     * @return
     */
    public static Bitmap compressImageBySize(String srcPath, float width, float height) {
        return compressImageBySize(srcPath, width, height, true);
    }

    /**
     * 压缩图片到指定尺寸（是否旋转）
     *
     * @param srcPath
     * @param width
     * @param height
     * @param rotate
     * @return
     */
    public static Bitmap compressImageBySize(String srcPath, float width, float height, boolean rotate) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = height; // 这里设置高度为800f
        float ww = width; // 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        // 旋转图片，归正
        if (rotate) {
            int imageDegree = getImageDegree(srcPath);
            bitmap = rotateImageView(imageDegree, bitmap);
        }
        return bitmap;
    }


    /**
     * 获取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return 旋转的角度
     */
    public static int getImageDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param degree 旋转角度
     * @param bitmap 将要旋转的bitmap
     * @return 旋转后的bitmap
     */
    public static Bitmap rotateImageView(int degree, Bitmap bitmap) {
        if (degree % 360 == 0) {
            return bitmap;
        }
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }



    public static Bitmap roundCorners(Bitmap bitmap, ImageView imageView, int roundPixels) {
        Bitmap roundBitmap;

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        int vw = imageView.getWidth();
        int vh = imageView.getHeight();
        if (vw <= 0) {
            vw = bw;
        }
        if (vh <= 0) {
            vh = bh;
        }

        int width, height;
        Rect srcRect;
        Rect destRect;
        switch (imageView.getScaleType()) {
            case CENTER_INSIDE:
                float vRation = (float) vw / vh;
                float bRation = (float) bw / bh;
                int destWidth;
                int destHeight;
                if (vRation > bRation) {
                    destHeight = Math.min(vh, bh);
                    destWidth = (int) (bw / ((float) bh / destHeight));
                } else {
                    destWidth = Math.min(vw, bw);
                    destHeight = (int) (bh / ((float) bw / destWidth));
                }
                int x = (vw - destWidth) / 2;
                int y = (vh - destHeight) / 2;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(x, y, x + destWidth, y + destHeight);
                width = vw;
                height = vh;
                break;
            case FIT_CENTER:
            case FIT_START:
            case FIT_END:
            default:
                vRation = (float) vw / vh;
                bRation = (float) bw / bh;
                if (vRation > bRation) {
                    width = (int) (bw / ((float) bh / vh));
                    height = vh;
                } else {
                    width = vw;
                    height = (int) (bh / ((float) bw / vw));
                }
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case CENTER_CROP:
                vRation = (float) vw / vh;
                bRation = (float) bw / bh;
                int srcWidth;
                int srcHeight;
                if (vRation > bRation) {
                    srcWidth = bw;
                    srcHeight = (int) (vh * ((float) bw / vw));
                    x = 0;
                    y = (bh - srcHeight) / 2;
                } else {
                    srcWidth = (int) (vw * ((float) bh / vh));
                    srcHeight = bh;
                    x = (bw - srcWidth) / 2;
                    y = 0;
                }
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
                destRect = new Rect(0, 0, width, height);
                break;
            case FIT_XY:
                width = vw;
                height = vh;
                srcRect = new Rect(0, 0, bw, bh);
                destRect = new Rect(0, 0, width, height);
                break;
            case CENTER:
            case MATRIX:
                width = Math.min(vw, bw);
                height = Math.min(vh, bh);
                x = (bw - width) / 2;
                y = (bh - height) / 2;
                srcRect = new Rect(x, y, x + width, y + height);
                destRect = new Rect(0, 0, width, height);
                break;
        }

        try {
            roundBitmap = getRoundedCornerBitmap(bitmap, roundPixels, srcRect, destRect, width, height);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            roundBitmap = bitmap;
        }

        return roundBitmap;
    }


    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }

    /**
     * 缩放图片
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scale = ((float) newWidth) / width;
        // float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postScale(scale, scale);
        // 得到新的图片
        try {
            Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            return newbm;
        } catch (IllegalArgumentException e) {
            return bm;
        }
    }

    /**
     * 获取圆角图片
     *
     * @param bitmap
     * @param roundPixels
     * @param srcRect
     * @param destRect
     * @param width
     * @param height
     * @return
     */
    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels, Rect srcRect, Rect destRect, int width, int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final RectF destRectF = new RectF(destRect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFF000000);
        canvas.drawRoundRect(destRectF, roundPixels, roundPixels, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, destRectF, paint);

        return output;
    }


    /**
     * 从布局获取图片
     *
     * @param v
     * @return
     */
//    public static Bitmap loadBitmapFromView(View v) {
//        v.clearFocus();
//        v.setPressed(false);
//        boolean willNotCache = v.willNotCacheDrawing();
//        v.setWillNotCacheDrawing(false);
//        int color = v.getDrawingCacheBackgroundColor();
//        v.setDrawingCacheBackgroundColor(0);
//        if (color != 0) {
//            v.destroyDrawingCache();
//        }
//        v.buildDrawingCache();
//        Bitmap cacheBitmap = v.getDrawingCache();
//        if (cacheBitmap == null) {
//            return null;
//        }
//        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
//        v.destroyDrawingCache();
//        v.setWillNotCacheDrawing(willNotCache);
//        v.setDrawingCacheBackgroundColor(color);
//        return bitmap;
//    }


    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    /**
     * 布局超出了屏幕的高度（Scrollview处理）
     * @param scrollView
     * @return
     */
    public static Bitmap loadBitmapByScrollView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 绘制微信小程序分享封面图 图片默认展示比例为 5:4
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap drawWXMiniBitmap(Bitmap bitmap, int width, int height) {
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 用这个Bitmap生成一个Canvas,然后canvas就会把内容绘制到上面这个bitmap中
        Canvas mCanvas = new Canvas(mBitmap);
        // 绘制画笔
        Paint mPicturePaint = new Paint();
        // 绘制背景图片
        mCanvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPicturePaint);
        // 绘制图片的宽、高
        int width_head = bitmap.getWidth();
        int height_head = bitmap.getHeight();
        // 绘制图片－－保证其在水平方向居中
        mCanvas.drawBitmap(bitmap, (width - width_head) / 2, (height - height_head) / 2,
                mPicturePaint);
        // 保存绘图为本地图片
        mCanvas.save();
        mCanvas.restore();
        return mBitmap;
    }



    /**
     * 布局转化为图片并保存本地
     *
     * @param mContext
     * @param view
     */
    public static void viewSaveToImage(Context mContext, View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);
        saveBitmap(mContext,cachebmp,true);
        view.destroyDrawingCache();
    }


    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * 保存图片到本地
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap bitmap,boolean isShowToast) {
        if (bitmap == null) {
            LogUtils.d("saveBitmap bitmap = null");
            return "";
        }
        boolean saveSuccess = false;
        String imagePath = "";
        String imageName="";
        File file=null;
        try {
            // SD卡根目录
            File sdRoot = FileUtils.getAppImgPath(context);
            imageName=Calendar.getInstance().getTimeInMillis() + ".png";
             file = new File(sdRoot, imageName);
            imagePath = file.getAbsolutePath();
            if (!file.exists()) {
                saveSuccess = FileUtils.bitmapToFile(bitmap, imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("imagePath=" + imagePath);

        if (saveSuccess){
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, imageName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri=Uri.parse(imagePath);
            intent.setData(uri);
            context.sendBroadcast(intent);
            if (isShowToast){
                ToastUtils.showToast(context,"保存成功");
            }
        }else {
            if (isShowToast){
                ToastUtils.showToast(context,"保存失败");
            }
        }
        return imagePath;
    }


    /**
     * 获取视频时长
     *
     * @param url
     * @return
     */
    public static int getVideoUrlDuration(String url) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int duration = mediaPlayer.getDuration();
        return duration;
    }
}
