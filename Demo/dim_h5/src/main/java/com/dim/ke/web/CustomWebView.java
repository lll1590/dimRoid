package com.dim.ke.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.dim.ke.web.core.TbsBridgeWebView;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.URLUtil;
import com.tencent.smtt.sdk.WebSettings;

public class CustomWebView extends TbsBridgeWebView {
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomWebView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        //移除默认内置接口,防止远程代码执行漏洞攻击
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");

        //不同设备点击WebView输入框键盘的不弹起
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                requestFocus(View.FOCUS_DOWN);
                return false;
            }
        });

        //三星手机硬件加速关闭后导致H5弹出的对话框出现不消失情况
        String brand = android.os.Build.BRAND;
        if ("samsung".equalsIgnoreCase(brand)) {
            if(getContext() instanceof Activity) {
                ((Activity) getContext()).getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        }

        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadFile(getContext(), url);
            }
        });

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + ",dim");

        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webSettings.setDatabaseEnabled(true);
        String dir = getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);


        //noinspection deprecation
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCachePath(getContext().getApplicationContext().getCacheDir().getAbsolutePath());
        webSettings.setDefaultTextEncodingName("UTF-8");
        //屏幕自适应
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setTextZoom(100);
    }

    private void downloadFile(Context context, String url){
        if(!URLUtil.isValidUrl(url)){
            Log.d("JYWebView", "非法下载地址");
            return;
        }

        Uri uri=Uri.parse(url);
        Intent intent=new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);

    }
}
