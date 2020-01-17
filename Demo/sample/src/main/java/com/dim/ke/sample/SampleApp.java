package com.dim.ke.sample;

import com.dim.ke.framework.core.DimApp;
import com.dim.ke.sample.dagger.app.AppComponent;
import com.dim.ke.sample.dagger.app.AppModule;
import com.dim.ke.sample.dagger.app.ComponentHolder;
import com.dim.ke.sample.dagger.app.DaggerAppComponent;

import javax.inject.Inject;

public class SampleApp extends DimApp {


    @Override
    public void onCreate() {
        super.onCreate();

        inject();
    }

    private void inject(){
        AppComponent appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        ComponentHolder.setAppComponent(appComponent);
    }
}
