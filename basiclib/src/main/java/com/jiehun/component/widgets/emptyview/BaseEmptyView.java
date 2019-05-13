package com.jiehun.component.widgets.emptyview;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.utils.AbDisplayUtil;


/**
 * Created by zhouyao on 17-12-18.
 */
public class BaseEmptyView extends BaseAttachToActivity {
    /**
     * @param targetView：被覆盖的View
     * @Description:显示空数据页面
     */
    private OverlayLayout mErrorOverlay;

    private OverlayLayout mEmtyOverlay;

    private OverlayLayout mLoadingOverlay;

    private OverlayLayout mGrayOverlay;

    private OverlayLayout mCustomOverlay;

    public BaseEmptyView(Activity mActivity) {
        attachToActivity(mActivity);
    }


    private View commonEmptyView;
    private View commonErrorView;

    /**
     * @param targetView
     * @param mOnClickListener
     */

    public void showEmptyView(View targetView, View.OnClickListener mOnClickListener) {
        showEmptyView(targetView, mOnClickListener, 0);
    }

    public void showEmptyView(View targetView, View.OnClickListener mOnClickListener, int marginTopOfDp) {
        if (mEmtyOverlay == null) {
            mEmtyOverlay = new OverlayLayout(mActivity);
            mEmtyOverlay.attachTo(targetView);

            initEmptyView();
//            commonEmtyView = LayoutInflater.from(mActivity).inflate(R.layout.common_empty_overlay, null);

            View rl_empty_overlay = commonEmptyView.findViewById(R.id.rl_empty_overlay);
            rl_empty_overlay.setOnClickListener(mOnClickListener);
            if (marginTopOfDp > 0) {
                LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) rl_empty_overlay.getLayoutParams();
                mLayoutParams.setMargins(0, AbDisplayUtil.dip2px(marginTopOfDp), 0, 0);
                rl_empty_overlay.setLayoutParams(mLayoutParams);
            }
            mEmtyOverlay.setOverlayView(commonEmptyView);
        }
        mEmtyOverlay.showOverlay();
    }


    /**
     * 隐藏数据空页面
     */
    public void hideEmptyView() {
        if (mEmtyOverlay != null) mEmtyOverlay.hideOverlay();
    }


    private void initEmptyView() {
        if (null == commonEmptyView) {
            commonEmptyView = LayoutInflater.from(mActivity).inflate(R.layout.common_empty_overlay, null);
        }
    }

    public void setEmptyViewData(String emptyData, @DrawableRes int imgRes) {
        initEmptyView();
        ImageView iv_empty_error = (ImageView) commonEmptyView.findViewById(R.id.iv_empty_error);
        TextView tv_empty_error = (TextView) commonEmptyView.findViewById(R.id.tv_empty_error);
        iv_empty_error.setImageResource(imgRes);
        tv_empty_error.setText(emptyData);

    }
    private void initErroriew() {
        if (null == commonErrorView) {
            commonErrorView = LayoutInflater.from(mActivity).inflate(R.layout.common_error_overlay, null);
        }
    }



    public void setErrorViewData(String emptyData, @DrawableRes int imgRes) {
        initErroriew();
        ImageView iv_empty_error = (ImageView) commonErrorView.findViewById(R.id.iv_empty_error);
        TextView tv_empty_error = (TextView) commonErrorView.findViewById(R.id.tv_empty_error);
        iv_empty_error.setImageResource(imgRes);
        tv_empty_error.setText(emptyData);
    }

    /**
     * @param targetView
     * @param mOnClickListener
     */

    public void showErrorView(View targetView, View.OnClickListener mOnClickListener) {
        showErrorView(targetView, mOnClickListener, 0);
    }

    public void showErrorView(View targetView, View.OnClickListener mOnClickListener, int marginTopOfDp) {
        if (mErrorOverlay == null) {
            mErrorOverlay = new OverlayLayout(mActivity);
            mErrorOverlay.attachTo(targetView);
//            commonErrorView = LayoutInflater.from(mActivity).inflate(R.layout.common_error_overlay, null);
            initErroriew();
            View rl_error_overlay = commonErrorView.findViewById(R.id.rl_error_overlay);
            rl_error_overlay.setOnClickListener(mOnClickListener);
            if (marginTopOfDp > 0) {
                LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) rl_error_overlay.getLayoutParams();
                mLayoutParams.setMargins(0, AbDisplayUtil.dip2px(marginTopOfDp), 0, 0);
                rl_error_overlay.setLayoutParams(mLayoutParams);
            }
            mErrorOverlay.setOverlayView(commonErrorView);
        }
        mErrorOverlay.showOverlay();
    }


    /**
     * 隐藏数据空页面
     */
    public void hideErrorView() {
        if (mErrorOverlay != null) mErrorOverlay.hideOverlay();
    }

    public void showCustomView(View targetView, View overlayView) {
        if (mCustomOverlay == null) {
            mCustomOverlay = new OverlayLayout(mActivity);
            mCustomOverlay.attachTo(targetView);
            mCustomOverlay.setOverlayView(overlayView);
        }
        mCustomOverlay.showOverlay();
    }

    public void hideCustomView() {
        if (mCustomOverlay != null) {
            mCustomOverlay.hideOverlay();
        }
    }

    public void showLoadingView(View targetView) {
        if (mLoadingOverlay == null) {
            mLoadingOverlay = new OverlayLayout(mActivity);
            mLoadingOverlay.attachTo(targetView);
            View commonLoadingView = LayoutInflater.from(mActivity).inflate(R.layout.common_loading_overlay, null);
//            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
//                    mActivity, R.anim.common_loading_animation);
//            // 使用ImageView显示动画
//            ImageView iv_progress = (ImageView) commonLoadingView.findViewById(R.id.iv_progress);
//            iv_progress.startAnimation(hyperspaceJumpAnimation);
            mLoadingOverlay.setOverlayView(commonLoadingView);
        }
        mLoadingOverlay.showOverlay();
    }

    /**
     * 隐藏数据空页面
     */
    public void hideLoadingView() {
        if (mLoadingOverlay != null) mLoadingOverlay.hideOverlay();
    }



    public void showGrayView(View targetView) {
        if (mGrayOverlay == null) {
            mGrayOverlay = new OverlayLayout(mActivity);
            mGrayOverlay.attachTo(targetView);
            View commonLoadingView = LayoutInflater.from(mActivity).inflate(R.layout.common_gray_overlay, null);
            mGrayOverlay.setOverlayView(commonLoadingView);
        }
        mGrayOverlay.showOverlay();
    }

    /**
     * 隐藏数据空页面
     */
    public void hideGrayView() {
        if (mGrayOverlay != null) mGrayOverlay.hideOverlay();
    }





}
