package com.dim.ke.sample.dagger;

import com.dim.ke.sample.dagger.app.AppComponent;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = DaggerModule.class, dependencies = AppComponent.class)
public interface DaggerComponent {
    void inject(DaggerDemoActivity demoActivity);
}
