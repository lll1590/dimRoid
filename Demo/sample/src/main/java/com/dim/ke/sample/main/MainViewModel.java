package com.dim.ke.sample.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.dim.ke.framework.core.ui.mvvm.viewmodel.BaseViewModel;
import com.dim.ke.sample.BR;
import com.dim.ke.sample.entity.Main;

public class MainViewModel extends BaseViewModel<MainModel> {

    @Bindable
    public String text = "demo";

    public MutableLiveData<Main> main = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application, MainModel model) {
        super(application, model);
    }

    public void bindClick(){

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(com.dim.ke.sample.BR.text);
    }
}
