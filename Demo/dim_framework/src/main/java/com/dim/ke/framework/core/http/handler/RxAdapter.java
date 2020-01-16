package com.dim.ke.framework.core.http.handler;

import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import com.dim.ke.framework.core.http.RetrofitManager;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: <Rx适配器><br>
 * Author:      mxdl<br>
 * Date:        2019/3/18<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class RxAdapter {

    /**
     * @param lifecycle
     * @return
     */
    public static ObservableTransformer apiTransformer(@NonNull final LifecycleProvider lifecycle){
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycle.bindUntilEvent(ActivityEvent.DESTROY));
            }
        };
    }

    /**
     * 生命周期绑定
     *
     * @param lifecycle Activity
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull LifecycleProvider lifecycle) {
        if (lifecycle != null) {
            return lifecycle.bindUntilEvent(ActivityEvent.DESTROY);
        } else {
            throw new IllegalArgumentException("context not the LifecycleProvider type");
        }
    }

    /**
     * 线程调度器
     */
    public static ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static ObservableTransformer exceptionTransformer() {

        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable observable) {
                return observable
                        .map(new HandleFuc())  //这里可以取出BaseResponse中的Result
                        .onErrorResumeNext(new HttpResponseFunc());
            }
        };
    }

    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable t) {
            ResponseThrowable exception = ExceptionHandler.handleException(t);
            return Observable.error(exception);
        }
    }

    private static class HandleFuc implements Function<Object,Object> {

        @Override
        public Object apply(Object o) throws Exception {
//            if(o instanceof RespDTO){
//                RespDTO respDTO = (RespDTO) o;
//                if(respDTO.code != ExceptionHandler.APP_ERROR.SUCC){
//                    Toast.makeText(RetrofitManager.mContext,respDTO.error,Toast.LENGTH_SHORT).show();
//                }
//            }
            return o;
        }
    }

}
