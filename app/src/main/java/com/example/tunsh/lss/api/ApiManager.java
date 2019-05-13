package com.example.tunsh.lss.api;

import com.jiehun.component.http.BaseApiManager;

import java.util.HashMap;

import rx.Observable;


public class ApiManager extends BaseApiManager {

    private ApiManagerImpl mApiManagerImpl;
    /**
     * 单例模式
     *
     * @return
     */
    private static class HelperHolder {
        public static final ApiManager helper = new ApiManager();
    }

    public static ApiManager getInstance(){
        return HelperHolder.helper;
    }

    @Override
    public void ipHostChange() {
//        initRetrofit(BaseHttpUrl.BASE_URL);
        initRetrofit("https://wanandroid.com/");
        mApiManagerImpl = create(ApiManagerImpl.class);
    }

    public Observable getInfo(HashMap<String, Object> map) {
        Observable observable = mApiManagerImpl.getInfo(map);
        return wrapObservable(observable);
    }





}
