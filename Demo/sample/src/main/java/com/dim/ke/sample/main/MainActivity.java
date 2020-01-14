package com.dim.ke.sample.main;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dim.ke.framework.core.ui.mvvm.BaseMvvmActivity;
import com.dim.ke.framework.core.util.LogUtils;
import com.dim.ke.sample.BR;
import com.dim.ke.sample.R;
import com.dim.ke.sample.SampleViewModelFactory;
import com.dim.ke.sample.databinding.ActivityMainBinding;

public class MainActivity extends BaseMvvmActivity<ActivityMainBinding, MainViewModel> {


    @Override
    public Class<MainViewModel> onBindViewModel() {
        return MainViewModel.class;
    }

    @Override
    public ViewModelProvider.Factory onBindViewModelFactory() {
        return SampleViewModelFactory.getInstance(getApplication());
    }

    @Override
    public void initViewObservable() {
//        mDataBinding.setMainModel(mViewModel);
        mDataBinding.setVariable(BR.mainActivity, this);
        mViewModel.main.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mDataBinding.text.setText(s);
            }
        });
    }

    @Override
    public int onBindVariableId() {
        return BR.mainModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public String initTitleName() {
        return "Sample";
    }

    public void bindClick(View view){
        LogUtils.d("111114351111");
//        mDataBinding.edit.setText("12345");
        mViewModel.setMain("12345");
    }
}
