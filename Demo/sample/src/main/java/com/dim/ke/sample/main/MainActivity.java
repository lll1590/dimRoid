package com.dim.ke.sample.main;

import androidx.lifecycle.ViewModelProvider;

import com.dim.ke.framework.core.ui.mvvm.BaseMvvmActivity;
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

}
