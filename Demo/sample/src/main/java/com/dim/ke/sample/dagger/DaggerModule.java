package com.dim.ke.sample.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerModule {
    DaggerDemoActivity daggerDemoActivity;

    public DaggerModule(DaggerDemoActivity daggerDemoActivity) {
        this.daggerDemoActivity = daggerDemoActivity;
    }

    @Provides
    DaggerBean providerDaggerBean(){
        return new DaggerBean();
    }
}
