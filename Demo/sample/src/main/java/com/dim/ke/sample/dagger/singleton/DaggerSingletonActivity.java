package com.dim.ke.sample.dagger.singleton;

import android.content.SharedPreferences;

import com.dim.ke.framework.core.ui.BaseActivity;
import com.dim.ke.framework.core.util.LogUtils;
import com.dim.ke.sample.R;
import com.dim.ke.sample.dagger.DaggerDaggerComponent;
import com.dim.ke.sample.dagger.DaggerModule;
import com.dim.ke.sample.dagger.app.ComponentHolder;

import javax.inject.Inject;

public class DaggerSingletonActivity extends BaseActivity {

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dagger_demo;
    }

    @Override
    public void initView() {
//        DaggerDaggerComponent.builder().appComponent(ComponentHolder.getAppComponent()).daggerModule(new DaggerModule(this)).build().inject(this);
    }

    @Override
    public void initData() {
        LogUtils.d("sharePreferences hash:" + sharedPreferences.hashCode());
    }

    @Override
    public void initListener() {

    }

    @Override
    public String initTitleName() {
        return null;
    }

    @Override
    public void destroyView() {

    }
}
