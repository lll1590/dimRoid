package com.dim.ke.framework.core.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import com.ng.framework.R;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class AndroidUtils {

    public static void sendEmail(Context context, String email) {
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

        String mySubject = "";
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mySubject);
        String myBodyText = "";
        emailIntent.putExtra(Intent.EXTRA_TEXT, myBodyText);
        context.startActivity(Intent.createChooser(emailIntent, ""));
    }


    /**
     * 打电话.
     *
     * @param context 上下文
     * @param telNum  电话号码
     */
    public static void call(Context context, String telNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
        context.startActivity(intent);
    }

    public static void showSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public static void hideSoftKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取当前app包信息对象.
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    private static PackageInfo getCurrentAppPackageInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前app的版本号.
     *
     * @param context
     * @return
     */
    public static int getCurrentAppVersionCode(Context context) {
        PackageInfo info = getCurrentAppPackageInfo(context);
        int versionCode = info.versionCode;
        return versionCode;
    }

    /**
     * 获取当前app的版本名字.
     *
     * @param context
     * @return
     */
    public static String getCurrentAppVersionName(Context context) {
        PackageInfo info = getCurrentAppPackageInfo(context);
        String version = info.versionName;
        return version;
    }

    /**
     * 获得当前App的包名
     *
     * @param context
     * @return pgName
     */
    public static String getAppPackageName(Context context) {
        PackageInfo info = getCurrentAppPackageInfo(context);
        String pgName = info.packageName;
        return pgName;
    }

    /**
     * 获取App的名称
     *
     * @param context 上下文
     * @return 名称
     */
    public static String getAppName(Context context) {
        return context.getResources().getString(R.string.app_name);
//        PackageManager pm = context.getPackageManager();
//        //获取包信息
//        try {
//            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
//            //获取应用 信息
//            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//            //获取albelRes
//            int labelRes = applicationInfo.labelRes;
//            //返回App的名称
//            return context.getResources().getString(labelRes);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return null;
    }

    /**
     * 获取手机的imei.
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        if (lacksPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return Settings.System.ANDROID_ID;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            return TextUtils.isEmpty(imei) ? Settings.System.ANDROID_ID : imei;
        }
    }

    /**
     * 判断是否缺少权限
     */
    public static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public boolean lacksPermissions(Context mContexts, String[] mPermissions) {
        for (String permission : mPermissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;

    }

    /**
     * 检测程序是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProessRunning(Context context, String proessName) {

        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {
            if (info.processName.equals(proessName)) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static void goToSetting(Context mContext) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getAppPackageName(mContext)));
        mContext.startActivity(intent);
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public static String getDeviceName() {
        return getDeviceBrand() + " " + getSystemModel();

    }

    public static String getNonceStr(int length) {
        Random random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        if (activity != null) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric;
        } else {
            throw new RuntimeException("Activity must not be null.");
        }
    }

    public static final String NormalPath="com.jy.t11.LoadingAliasNormalActivity";
    public static final String TPLUSPath = "com.jy.t11.LoadingAliasTplusActivity";


    /**
     * 启用组件 *
     * @param componentNameStr
     * 重要方法
     */
    public static void enableComponent(Context context, String componentNameStr) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, componentNameStr);
        LogUtils.d("enableComponent",componentName.getClassName());
        int state = pm.getComponentEnabledSetting(componentName);
        if (PackageManager.COMPONENT_ENABLED_STATE_ENABLED == state) {
            //已经启用
            return;
        }
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }


    /**
     * 禁用组件
     * @param context
     * @param componentNameStr
     */
    public static void disableComponent(Context context, String componentNameStr) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, componentNameStr);
        int state = pm.getComponentEnabledSetting(componentName);
        if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED == state) {
            //已经禁用
            return;
        }
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


    /**
     * 更换app图标
     * @param context
     * @param fromComponentName
     * @param toComponentName
     */
    public static void changeAppIcon(Context context,String fromComponentName,String toComponentName){
        disableComponent(context,fromComponentName);
        enableComponent(context,toComponentName);
    }







}
