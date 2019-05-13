package com.jiehun.component.widgets.emptyview;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.jiehun.component.http.exception.NetLinkException;
import com.jiehun.component.utils.AbPreconditions;

import java.util.List;

/**
 * Created by zhouyao on 17-12-18.
 */
public class AbEmptyViewHelper {

    private View needOverlayView;

    private   Activity      activity;
    protected BaseEmptyView mBaseEmptyView;//空白页
    private   View          overlayView;


    public AbEmptyViewHelper(View needOverlayView, Activity activity) {
        this.needOverlayView = needOverlayView;
        this.activity = activity;
    }

    public AbEmptyViewHelper(View needOverlayView, View overlayView, Activity activity) {
        this.needOverlayView = needOverlayView;
        this.overlayView = overlayView;
        this.activity = activity;
    }

    public synchronized BaseEmptyView getBaseEmptyView() {
        if (null == mBaseEmptyView) {
            mBaseEmptyView = new BaseEmptyView(activity);
        }
        return mBaseEmptyView;
    }


    public void beginRefresh() {
        hideEmptyView();
        getBaseEmptyView().showLoadingView(needOverlayView);
    }

    /**
     * 错误页等等只对　　刷新操作有用
     *
     * @param isRefresh
     * @param newlist
     * @param mRetrylis
     */
    public void endRefreshOnSuccess(boolean isRefresh, List newlist, View.OnClickListener mRetrylis) {
        hideEmptyView();
        if (isRefresh) {
            if (AbPreconditions.checkNotEmptyList(newlist)) {

            } else {
                getBaseEmptyView().showEmptyView(needOverlayView, mRetrylis);
            }
        }
    }


    /**
     * 错误页等等只对　　刷新操作有用
     *
     * @param isRefresh
     * @param newlistSize
     * @param mRetrylis
     */
    public void endRefreshOnSuccess(boolean isRefresh, int newlistSize, View.OnClickListener mRetrylis) {
        hideEmptyView();
        if (isRefresh) {
            if (newlistSize > 0) {

            } else {
                getBaseEmptyView().showEmptyView(needOverlayView, mRetrylis);
            }
        }
    }

    public void endRefreshOnFail(boolean isRefresh, Throwable e, View.OnClickListener mRetrylis) {
        hideEmptyView();
        if (isRefresh) {
            if (e instanceof NetLinkException) {
                getBaseEmptyView().showErrorView(needOverlayView, mRetrylis);
            }

            getBaseEmptyView().showErrorView(needOverlayView, mRetrylis);
        }

    }

    /**
     * 结束刷新。
     *
     * @param list
     * @param e
     * @param mRetrylis
     */
    public void endRefresh(List list, Throwable e, View.OnClickListener mRetrylis) {
        hideEmptyView();

        //只要存在数据，则不改变界面的展示。不显示覆盖页面。
        if (list == null || list.size() == 0) {

            if (e != null) {
                getBaseEmptyView().showErrorView(needOverlayView, mRetrylis);
            } else {
                getBaseEmptyView().showEmptyView(needOverlayView, mRetrylis);
            }

        }

    }

    public void attachOverlay(View overlayView) {
        hideEmptyView();
        if (overlayView == null) {
            return;
        }
        getBaseEmptyView().showCustomView(needOverlayView, overlayView);
    }


    /**
     * @param e
     * @param mRetrylis
     */
    public void endRefreshNotList(Throwable e, View.OnClickListener mRetrylis) {
        hideEmptyView();
        //只要存在数据，则不改变界面的展示。不显示覆盖页面。
        if (e != null) {
            getBaseEmptyView().showErrorView(needOverlayView, mRetrylis);
        } else {
//                getBaseEmptyView().showEmptyView(needOverlayView, mRetrylis);
        }
    }


    public void showGrayView(boolean showEmpty) {
        hideEmptyView();
        //只要存在数据，则不改变界面的展示。不显示覆盖页面。
        if (showEmpty) {
            getBaseEmptyView().showGrayView(needOverlayView);
        }
    }

    public void hideEmptyView() {
        getBaseEmptyView().hideEmptyView();
        getBaseEmptyView().hideErrorView();
        getBaseEmptyView().hideLoadingView();
        getBaseEmptyView().hideGrayView();
        getBaseEmptyView().hideCustomView();
    }


    /**
     * 设置内容空白或错误的  错误页跟空白页统一的方法
     * @param emptyData
     * @param imgRes
     */
    public void setEmptyAnErrorViewData(String emptyData, @DrawableRes int imgRes){
        setEmptyViewData(emptyData, imgRes);
        setErrorViewData(emptyData, imgRes);
    }


    /**
     * 设置内容空白页统一的方法
     * @param emptyData
     * @param imgRes
     */
    public void setEmptyViewData(String emptyData, @DrawableRes int imgRes) {
        getBaseEmptyView().setEmptyViewData(emptyData, imgRes);
    }

    /**
     * 设置内容错误页统一的方法
     * @param emptyData
     * @param imgRes
     */
    public void setErrorViewData(String emptyData, @DrawableRes int imgRes) {
        getBaseEmptyView().setErrorViewData(emptyData, imgRes);
    }
}
