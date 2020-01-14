package com.dim.ke.framework.core.ui.mvvm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dim.ke.framework.core.ui.BaseActivity;
import com.dim.ke.framework.core.ui.mvvm.viewmodel.BaseViewModel;

import java.util.Map;

public abstract class BaseMvvmActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity {

    protected V mDataBinding;
    protected VM mViewModel;
    private int viewModelId;

    public abstract Class<VM> onBindViewModel();
    public abstract ViewModelProvider.Factory onBindViewModelFactory();
    public abstract void initViewObservable();
    public abstract int onBindVariableId();

    public VM createViewModel(){
        return ViewModelProviders.of(this,onBindViewModelFactory()).get(onBindViewModel());
    }

    protected void initBaseViewObservable() {
        mViewModel.getUC().getShowPageStateViewEvent().observe(this, new Observer<BaseViewModel.PageState>() {
            @Override
            public void onChanged(@Nullable BaseViewModel.PageState show) {
                if(show == BaseViewModel.PageState.showContentView) {
                    showContentView();
                }else if(show == BaseViewModel.PageState.showEmptyView){
                    showEmptyView();
                }else if(show == BaseViewModel.PageState.showErrView){
                    showLoadErrView();
                }else if(show == BaseViewModel.PageState.showLoadingView){
                    showLoadingView();
                }
            }
        });
        mViewModel.getUC().getStartActivityEvent().observe(this, new Observer<Map<String, Object>>() {
            @Override
            public void onChanged(@Nullable Map<String, Object> params) {
                Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                startActivity(clz, bundle);
            }
        });
        mViewModel.getUC().getFinishActivityEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                finish();
            }
        });
        mViewModel.getUC().getOnBackPressedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                onBackPressed();
            }
        });
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void initContentView() {
        initViewDataBinding();
        initBaseViewObservable();
        initViewObservable();

    }

    private void initViewDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModelId = onBindVariableId();
        mViewModel = createViewModel();
        if(mDataBinding != null){
            mDataBinding.setVariable(viewModelId, mViewModel);
            mDataBinding.setLifecycleOwner(this);
        }
        getLifecycle().addObserver(mViewModel);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {

    }

    @Override
    public void destroyView() {
        if(mDataBinding != null){
            mDataBinding.unbind();
        }
    }
}
