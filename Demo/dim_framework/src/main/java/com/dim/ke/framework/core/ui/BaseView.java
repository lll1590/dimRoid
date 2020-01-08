package com.dim.ke.framework.core.ui;

import android.content.Context;

public interface BaseView {
    Context getContext();
    int getLayoutId();
    void beforeOnCreate();
    void initView();
    void initData();
    void initListener();
    void destroyView();

}
