package com.dim.ke.sample.dagger.app;

import android.content.SharedPreferences;

import com.dim.ke.sample.SampleApp;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    SampleApp getApp();
    SharedPreferences getSP();
}
