package com.dim.ke.framework.core.util;


import com.dim.ke.framework.core.Constant;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class LogUtils {
    public final static String TAG = "JY_Android";
    public final static boolean DEBUG = Constant.DEBUG;

    /**
     * 初始化log工具，在app入口处调用
     *
     * @param isLogEnable 是否打印log
     */
    public static void init(boolean isLogEnable) {
        Logger.init(TAG)
                .hideThreadInfo()
                .logLevel(isLogEnable ? LogLevel.FULL : LogLevel.NONE)
                .logLevel(LogLevel.FULL)
                .methodOffset(2);
    }

    public static void v(String tag, String message){
        Logger.v(tag + ":" + message);
    }

    public static void d(String tag, String message){
        if(!DEBUG)return;
        Logger.d(tag + ":" + message);
    }

    public static void d(String message) {
        if(!DEBUG)return;
        Logger.d(message == null ? "" : message);
    }

    public static void i(String tag, String message) {
        Logger.i(tag + ":" + message);
    }

    public static void i(String message) {
        Logger.i(message);
    }

    public static void e(String tag, String message) {
        Logger.e(tag + ":" + message);
    }

    public static void w(String tag, String message){
        Logger.d(tag + ":" + message);
    }

    public static void w(String message, Throwable e) {
        String info = e != null ? e.toString() : "null";
        Logger.w(message == null ? "" : message + "：" + info);
    }

    public static void e(String message, Throwable e) {
        Logger.e(e, message == null ? "" : message);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void w(String message) {
        Logger.w(message == null ? "" : message);
    }
    public static void e(String message) {
        Logger.e(message == null ? "" : message);
    }
}
