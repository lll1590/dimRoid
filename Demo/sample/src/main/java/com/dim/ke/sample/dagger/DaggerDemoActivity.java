package com.dim.ke.sample.dagger;

import android.content.SharedPreferences;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dim.ke.framework.core.ui.BaseActivity;
import com.dim.ke.framework.core.util.LogUtils;
import com.dim.ke.sample.R;
import com.dim.ke.sample.SampleApp;
import com.dim.ke.sample.dagger.app.ComponentHolder;

import javax.inject.Inject;

@Route(path = "/dagger/demo")
public class DaggerDemoActivity extends BaseActivity {

    @Inject
    DaggerBean daggerBean;
    @Inject
    SampleApp app;
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dagger_demo;
    }

    @Override
    public void initView() {
        DaggerDaggerComponent.builder()
                .daggerModule(new DaggerModule(this))
                .appComponent(ComponentHolder.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        LogUtils.d("dagger:" + daggerBean.name);
        LogUtils.d("app is " + (app == null ? "null" : "not null"));
        LogUtils.d("sharePreferences hash:" + sharedPreferences.hashCode());
    }

    @Override
    public void initListener() {

    }

    @Override
    public String initTitleName() {
        return "Dagger";
    }

    @Override
    public void destroyView() {

    }
}
