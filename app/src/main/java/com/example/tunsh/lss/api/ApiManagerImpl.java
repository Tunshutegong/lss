package com.example.tunsh.lss.api;

import com.example.tunsh.lss.bean.WeatherResult;
import com.jiehun.component.http.JHHttpResult;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


public interface ApiManagerImpl {

    //糖果首页  http://www.weather.com.cn/
    @GET("wxarticle/list/408/1/json")//
    Observable<Response<JHHttpResult<WeatherResult>>> getInfo(@QueryMap HashMap<String, Object> map);


}
