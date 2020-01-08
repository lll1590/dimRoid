package com.dim.ke.web.core;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;

/**
 * Created by Administrator on 2017/3/29.
 */

public class WebViewFileChoseUtils {

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    private static ValueCallback<Uri> mUploadMessage;
    private static ValueCallback<Uri[]> mUploadMessageForAndroid5;

    public static void fileChose(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    public static void fileChose5(ValueCallback<Uri[]> uploadMsg) {
        openFileChooserImplForAndroid5(uploadMsg);
    }

    private static void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooser = Intent.createChooser(i, "File Chooser");
//        startActivityForResult(chooser, FILECHOOSER_RESULTCODE);
        if(ll != null){
            ll.startActivity(chooser, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
        }
    }

    private static void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        if(ll != null){
            ll.startActivity(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
        }
//        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    public static void onActivityResult(int requestCode, int resultCode,
                                        Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null
                    : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[] { result });
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
            }
            mUploadMessageForAndroid5 = null;
        }
    }


    public static ChooserFileImpl ll = null;
    public interface ChooserFileImpl{
        void startActivity(Intent intent, int requestCode);
    }
}
