package com.dim.ke.framework.core.ui.mvvm.model;

import android.app.Application;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseModel implements IBaseModel{
    protected Application mApp;
    private CompositeDisposable mCompositeDisposable;

    public BaseModel(Application mApp) {
        this.mApp = mApp;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public void addSubscribe(Disposable disposable){
        if(mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onCleared() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
