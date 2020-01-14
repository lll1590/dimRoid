package com.dim.ke.sample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dim.ke.sample.main.MainModel;
import com.dim.ke.sample.main.MainViewModel;

public class SampleViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile SampleViewModelFactory INSTANCE;
    private final Application mApplication;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    private SampleViewModelFactory(@NonNull Application application) {
        mApplication = application;
    }

    public static SampleViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (SampleViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SampleViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(mApplication, new MainModel(mApplication));
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
