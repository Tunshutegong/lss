package com.example.tunsh.lss;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.tunsh.lss.api.ApiManager;
import com.example.tunsh.lss.bean.WeatherResult;
import com.jiehun.component.base.BaseActivity;
import com.jiehun.component.http.HttpResult;
import com.jiehun.component.http.NetSubscriber;
import com.jiehun.component.rxjavabaselib.RxAppCompatActivity;
import com.jiehun.component.utils.AbRxJavaUtils;
import com.jiehun.component.utils.AbStorageManager;

import java.util.HashMap;

import rx.Observable;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener {
    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private LinearLayout li_menu0;
    private LinearLayout li_menu1;
    private LinearLayout li_menu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        if(fragment1 == null){
            fragment1 = new Fragment1();
        }
        replaceFragmentByTag(R.id.fl_contain,fragment1,"1");
        findViewById(R.id.li_menu0).setOnClickListener(this);
        findViewById(R.id.li_menu1).setOnClickListener(this);
        findViewById(R.id.li_menu2).setOnClickListener(this);
        AbStorageManager.getInstance().initCache();
        getList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.li_menu0:
                if(fragment1 == null){
                    fragment1 = new Fragment1();
                }
                replaceFragmentByTag(R.id.fl_contain,fragment1,"1");
                break;
            case R.id.li_menu1:
                if(fragment2 == null){
                    fragment2 = new Fragment2();
                }
                replaceFragmentByTag(R.id.fl_contain,fragment2,"2");
                break;
            case R.id.li_menu2:
                if(fragment3 == null){
                    fragment3 = new Fragment3();
                }
                replaceFragmentByTag(R.id.fl_contain,fragment3,"3");
                break;
        }
    }

    public void replaceFragmentByTag(@IdRes int layoutId, Fragment fragment, String tag) {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutId, fragment, tag);
        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public void getList() {

        HashMap<String, Object> map = new HashMap<>();
        Observable observable;
        observable = ApiManager.getInstance().getInfo(map);
        AbRxJavaUtils.toSubscribe(observable, bindToLifecycle(), new NetSubscriber<WeatherResult>() {
            @Override
            public void onNext(HttpResult<WeatherResult> result) {
                Log.e("zym","----->"+result.getData().getCurPage());

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

            }

            @Override
            public void commonCall(Throwable e) {
                super.commonCall(e);
            }
        });
    }

}
