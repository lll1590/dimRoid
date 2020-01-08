package com.dim.ke.framework.core;

import android.app.Application;

public class DimApp extends Application {

    private static DimApp mInstance;
    public static DimApp getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


    }
}
