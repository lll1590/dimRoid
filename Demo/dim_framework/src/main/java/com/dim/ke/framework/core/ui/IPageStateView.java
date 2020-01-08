package com.dim.ke.framework.core.ui;

public interface IPageStateView {
    /**
     * 加载中回调
     */
    void showLoadingView();

    /**
     * 加载失败回调
     */
    void showEmptyView();

    /**
     * 加载错误回调
     */
    void showLoadErrView();
}
