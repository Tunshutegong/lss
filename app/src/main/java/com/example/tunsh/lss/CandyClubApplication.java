package com.example.tunsh.lss;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.utils.AbStorageManager;

public class CandyClubApplication extends BaseApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        BaseLibConfig.initRxLib(this, true, BuildConfig.BUILD_TYPE, "1.0", true);
        AbStorageManager.getInstance().initCache();
    }


//    /**
//     * 注册x5内核
//     */
//    private void initTbsX5() {
//        ComponentManager componentManager = ComponentManager.getInstance();
//
//        if (componentManager.getService(WebviewService.class.getSimpleName()) != null) {
//            WebviewService webviewService = (WebviewService) componentManager.getService(WebviewService.class.getSimpleName());
//            webviewService.initTbsX5(this);
//        }
//    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
