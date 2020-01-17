package com.dim.ke.sample.dagger.app;

public class ComponentHolder {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static void setAppComponent(AppComponent appComponent) {
        ComponentHolder.appComponent = appComponent;
    }
}
