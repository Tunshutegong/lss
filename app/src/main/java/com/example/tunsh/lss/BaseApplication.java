package com.example.tunsh.lss;

import android.app.Application;
import android.content.pm.ApplicationInfo;


/**
 * Created by zhouyao
 * on 2017/12/6.
 */

public class BaseApplication extends Application {
    private static BaseApplication mBaseApplication;

    public static BaseApplication getInstance() {
        return mBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
        }
        mBaseApplication = this;
    }

    private boolean isDebug(){
        return
                (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ? true : false;
    }

}
