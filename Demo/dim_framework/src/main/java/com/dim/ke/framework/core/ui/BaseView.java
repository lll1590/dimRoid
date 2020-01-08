package com.dim.ke.framework.core.ui;

import android.content.Context;

public interface BaseView extends IPageStateView{
    Context getContext();
    int getLayoutId();
    default void beforeOnCreate(){};
    void initView();
    void initData();
    void initListener();
    String initTitleName();
    void destroyView();
}
