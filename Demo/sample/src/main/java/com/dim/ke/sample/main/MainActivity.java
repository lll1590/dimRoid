package com.dim.ke.sample.main;

import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dim.ke.framework.core.http.RetrofitManager;
import com.dim.ke.framework.core.ui.mvvm.BaseMvvmActivity;
import com.dim.ke.framework.core.util.LogUtils;
import com.dim.ke.sample.R;
import com.dim.ke.sample.SampleViewModelFactory;
import com.dim.ke.sample.databinding.ActivityMainBinding;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseMvvmActivity<ActivityMainBinding, MainViewModel> {


    @Override
    public Class<MainViewModel> onBindViewModel() {
        return MainViewModel.class;
    }

    @Override
    public ViewModelProvider.Factory onBindViewModelFactory() {
        return SampleViewModelFactory.getInstance(getApp());
    }

    @Override
    public void initViewObservable() {
//        mDataBinding.setMainModel(mViewModel);
        mDataBinding.setVariable(com.dim.ke.sample.BR.mainActivity, this);
        mViewModel.main.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mDataBinding.text.setText(s);
            }
        });
    }

    @Override
    public int onBindVariableId() {
        return com.dim.ke.sample.BR.mainModel;
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

    public void testApi(View view){
        Observable<String> testObservable = RetrofitManager.getInstance().getWanAndroidService().wsArticle();
        testObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d("onSubscribe...");
            }

            @Override
            public void onNext(String s) {
                LogUtils.d("onNext:" + s);
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d("onError:" + e.toString());
            }

            @Override
            public void onComplete() {
                LogUtils.d("onComplete...");
            }
        });
    }

    public void testDagger(View view){
        ARouter.getInstance().build("/dagger/demo").navigation();
    }
}
