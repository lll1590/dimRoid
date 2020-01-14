package com.dim.ke.framework.core.pageState;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import static android.view.View.NO_ID;

/**
 * Created by hupei on 2018/9/27 10:16.
 */
class PageStateLayoutCreater implements PageState {

    private Context mContext;
    private ViewGroup mRootView;
    private View mContentView, mLoadingView, mEmptyView, mErrorView;
    private View mLoadingProgressView, mEmptyImgView, mErrorImgView;
    private View mLoadingMsgView, mEmptyMsgView, mErrorMsgView;
    private OnErrorClickListener mOnErrorClickListener;
    private boolean showClickLoadView = true;
    private int mLoadingLayout = NO_ID, mEmptyLayout = NO_ID, mErrorLayout = NO_ID;
    private int mLoadingProgressViewId = NO_ID, mEmptyImgId = NO_ID, mErrorImgId = NO_ID;
    private int mLoadingMsgViewId = NO_ID, mEmptyMsgViewId = NO_ID, mErrorMsgViewId = NO_ID;

    @Override
    public PageState setLoadingLayout(@LayoutRes int loadingLayoutId) {
        this.mLoadingLayout = loadingLayoutId;
        return this;
    }

    @Override
    public PageState setEmptyLayout(@LayoutRes int emptyLayoutId) {
        this.mEmptyLayout = emptyLayoutId;
        return this;
    }

    @Override
    public PageState setErrorLayout(@LayoutRes int errorLayoutId) {
        this.mErrorLayout = errorLayoutId;
        return this;
    }

    @Override
    public PageState setLoadingProgressViewId(int loadingProgressViewId) {
        this.mLoadingProgressViewId = loadingProgressViewId;
        return this;
    }

    @Override
    public PageState setLoadingMsgViewId(int loadingMsgViewId) {
        this.mLoadingMsgViewId = loadingMsgViewId;
        return this;
    }

    @Override
    public PageState setEmptyImgId(int emptyImgId) {
        this.mEmptyImgId = emptyImgId;
        return this;
    }

    @Override
    public PageState setEmptyMsgViewId(@IdRes int emptyMsgViewId) {
        this.mEmptyMsgViewId = emptyMsgViewId;
        return this;
    }

    @Override
    public PageState setErrorImgId(int errorImgId) {
        this.mErrorImgId = errorImgId;
        return this;
    }

    @Override
    public PageState setErrorMsgViewId(@IdRes int errorMsgViewId) {
        this.mErrorMsgViewId = errorMsgViewId;
        return this;
    }

    @Override
    public PageState setClickShowLoadView(boolean show) {
        this.showClickLoadView = show;
        return this;
    }

    @Override
    public PageState setOnErrorListener(OnErrorClickListener listener) {
        this.mOnErrorClickListener = listener;
        return this;
    }

    @Override
    public void showLoadingView() {
        goneAllView();
        if (this.mLoadingView != null) {
            this.mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showContentView() {
        goneAllView();
        if (this.mContentView != null) {
            this.mContentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEmptyView() {
        goneAllView();
        if (this.mEmptyView != null) {
            this.mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorView() {
        goneAllView();
        if (mErrorView != null) mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public View getEmptyView() {
        return this.mEmptyView;
    }

    @Override
    public View getErrorView() {
        return this.mErrorView;
    }

    @Override
    public View getLoadingMsgView() {
        return this.mLoadingMsgView;
    }

    @Override
    public View getEmptyMsgView() {
        return this.mEmptyMsgView;
    }

    @Override
    public View getErrorMsgView() {
        return this.mErrorMsgView;
    }

    @Override
    public View getLoadingProgressView() {
        return this.mLoadingProgressView;
    }

    @Override
    public View getEmptyImgView() {
        return this.mEmptyImgView;
    }

    @Override
    public View getErrorImgView() {
        return this.mErrorImgView;
    }

    private void goneAllView() {
        if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
        if (mContentView != null) mContentView.setVisibility(View.GONE);
        if (mEmptyView != null) mEmptyView.setVisibility(View.GONE);
        if (mErrorView != null) mErrorView.setVisibility(View.GONE);
    }

    void setRootView(View rootView) {
        this.mRootView = (ViewGroup) rootView;
        this.mContext = rootView.getContext();
    }

    void setContentView(@IdRes int contentId) {
        setContentView(mRootView.findViewById(contentId));
    }

    void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    void create() {
        checkParams();
        this.mLoadingView = inflate(mLoadingLayout);
        this.mEmptyView = inflate(mEmptyLayout);
        this.mErrorView = inflate(mErrorLayout);

        final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT);
        this.mRootView.addView(mLoadingView, layoutParams);
        this.mRootView.addView(mEmptyView, layoutParams);
        this.mRootView.addView(mErrorView, layoutParams);

        if (this.mLoadingProgressViewId != NO_ID) {
            this.mLoadingProgressView = mLoadingView.findViewById(mLoadingProgressViewId);
        }
        if (this.mLoadingMsgViewId != NO_ID) {
            this.mLoadingMsgView = mLoadingView.findViewById(mLoadingMsgViewId);
        }
        if (this.mEmptyImgId != NO_ID) {
            this.mEmptyImgView = mEmptyView.findViewById(mEmptyImgId);
        }
        if (this.mEmptyMsgViewId != NO_ID) {
            this.mEmptyMsgView = mEmptyView.findViewById(mEmptyMsgViewId);
        }

        if (this.mOnErrorClickListener != null) {
            this.mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showClickLoadView) {
                        showLoadingView();
                    } else {
                        goneAllView();
                    }
                    mOnErrorClickListener.onErrorClick();
                }
            });
        }
        if (this.mErrorImgId != NO_ID) {
            this.mErrorImgView = mErrorView.findViewById(mErrorImgId);
        }
        if (this.mErrorMsgViewId != NO_ID) {
            this.mErrorMsgView = mErrorView.findViewById(mErrorMsgViewId);
        }

        goneAllView();
    }

    private void checkParams() {
        if (this.mRootView == null) {
            throw new NullPointerException("mRootView cannot be null. Please use use setRootView");
        }
        if (this.mLoadingLayout == NO_ID) {
            throw new IllegalArgumentException("mLoadingLayout cannot be null. Please use use " +
                    "psl_loadingLayout or extends PageStateDelegate#getLoadingLayout");
        }
        if (this.mEmptyLayout == NO_ID) {
            throw new IllegalArgumentException("mEmptyLayout cannot be null. Please use use " +
                    "psl_loadingLayout or extends PageStateDelegate#getEmptyLayout");
        }
        if (this.mErrorLayout == NO_ID) {
            throw new IllegalArgumentException("mErrorLayout cannot be null. Please use use " +
                    "psl_loadingLayout or extends PageStateDelegate#getErrorLayout");
        }
    }

    private View inflate(int layoutId) {
        return LayoutInflater.from(this.mContext).inflate(layoutId, this.mRootView, false);
    }
}
