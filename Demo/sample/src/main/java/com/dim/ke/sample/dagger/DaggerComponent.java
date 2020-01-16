package com.dim.ke.sample.dagger;

import dagger.Component;

@Component(modules = DaggerModule.class)
public interface DaggerComponent {
    void inject(DaggerDemoActivity demoActivity);
}
