package com.dim.ke.sample.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.dim.ke.framework.core.ui.mvvm.viewmodel.BaseViewModel;

public class MainViewModel extends BaseViewModel<MainModel> {

    @Bindable
    public String text = "demo";

    public MutableLiveData<String> main = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application, MainModel model) {
        super(application, model);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(com.dim.ke.sample.BR.text);
    }

    public MutableLiveData<String> getMain() {
        if(this.main == null){
            this.main = new MutableLiveData<>();
        }
        return main;
    }

    public void setMain(String main) {
        if(this.main == null){
            this.main = new MutableLiveData<>();
        }
        this.main.postValue(main);
    }
}
