package com.jiehun.component.widgets.emptyview;

import android.app.Activity;

/**
 * Created by zhouyao on 17-12-18.
 * 配合FragmentLifecycleCallbacks
 */
public class BaseAttachToActivity {
    public Activity mActivity;


    public void attachToActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

}
