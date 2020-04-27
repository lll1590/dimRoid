package com.dim.ke.sample.dagger.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.dim.ke.sample.SampleApp;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private SampleApp app;

    public AppModule(SampleApp app) {
        this.app = app;
    }

    @Provides
    public SampleApp getApp(){
        return app;
    }

    @Provides
    public SharedPreferences getSP(){
        return app.getSharedPreferences("dim_sp", Context.MODE_PRIVATE);
    }
}
