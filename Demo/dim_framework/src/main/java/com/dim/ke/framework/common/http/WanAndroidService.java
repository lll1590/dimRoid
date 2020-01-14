package com.dim.ke.framework.common.http;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface WanAndroidService {
    String WanAndroidApi = "https://www.wanandroid.com";

    @GET("/wxarticle/chapters/json")
    Observable<String> wsArticle();
}
