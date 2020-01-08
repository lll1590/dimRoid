package com.dim.ke.framework.core;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dim.ke.framework.core.util.LogUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

public class DimApp extends Application {

    private static DimApp mInstance;
    public static DimApp getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        initARouter();
        // 初始化Looger工具
        LogUtils.init(Constant.DEBUG);
        //auttLayout初始化
        AutoLayoutConifg.getInstance().useDeviceSize();
    }


    /**
     * ARouter组件初始化
     */
    private void initARouter() {
        if (Constant.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);

    }
}
