package com.jiehun.component.widgets.emptyview;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiehun.component.basiclib.R;
import com.jiehun.component.config.BaseLibConfig;
import com.jiehun.component.utils.AbDisplayUtil;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.widgets.recycleview.RecycleAdapterState;
import com.jiehun.component.widgets.recycleview.RecyclerAdapterWithHF;

/**
 * Created by zhouyao on 17-12-18.
 */

public class RecyclerEmptyView {

    private View commonEmptyView;


    private int minHeight = 200;//dp

    private RecyclerAdapterWithHF mRecyclerAdapterWithHF;//加载更多

    public RecyclerEmptyView setEmptyView(View.OnClickListener mOnClickListener, int headHeightOfDp, int marginTopOfDp) {
        initEmptyView();
        View rl_empty_overlay = commonEmptyView.findViewById(R.id.rl_empty_overlay);
        rl_empty_overlay.setOnClickListener(mOnClickListener);

        if (headHeightOfDp > 0 || marginTopOfDp > 0) {

            LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) rl_empty_overlay.getLayoutParams();
            //高度
            if (headHeightOfDp > 0) {
                mLayoutParams.weight = BaseLibConfig.UI_WIDTH;
                mLayoutParams.height = Math.max(BaseLibConfig.UI_HEIGHT - AbDisplayUtil.dip2px(headHeightOfDp), AbDisplayUtil.dip2px(minHeight));
            }
            if (marginTopOfDp > 0) {
                //布局　间距
                mLayoutParams.setMargins(0, AbDisplayUtil.dip2px(marginTopOfDp), 0, 0);
            }
            rl_empty_overlay.setLayoutParams(mLayoutParams);
        }
        return this;
    }


    public RecyclerEmptyView setEmptyViewData(CharSequence emptyData, @DrawableRes int imgRes) {
        initEmptyView();
        ImageView iv_empty_error = (ImageView) commonEmptyView.findViewById(R.id.iv_empty_error);
        TextView tv_empty_error = (TextView) commonEmptyView.findViewById(R.id.tv_empty_error);
        iv_empty_error.setImageResource(imgRes);
        tv_empty_error.setText(emptyData);
        return this;
    }


    public RecyclerEmptyView showEmptyView(RecyclerAdapterWithHF mRecyclerAdapterWithHF) {
        initRecyclerAdapterWithHF(mRecyclerAdapterWithHF);
        initEmptyView();
        addEmptyViewToHeader();
        return this;
    }

    public RecyclerEmptyView hideEmptyView(RecyclerAdapterWithHF mRecyclerAdapterWithHF) {
        initRecyclerAdapterWithHF(mRecyclerAdapterWithHF);
        initEmptyView();
        removeEmptyViewFromHeader();
        return this;
    }


    private void addEmptyViewToHeader() {
        mRecyclerAdapterWithHF.addHeader(commonEmptyView);
        mRecyclerAdapterWithHF.setState(RecycleAdapterState.State.STATE_EMPTY);
    }

    private void removeEmptyViewFromHeader() {
        mRecyclerAdapterWithHF.removeHeader(commonEmptyView);
        mRecyclerAdapterWithHF.setState(RecycleAdapterState.State.STATE_NORMAL);
    }

    private void initRecyclerAdapterWithHF(RecyclerAdapterWithHF mRecyclerAdapterWithHF){
        if(null == this.mRecyclerAdapterWithHF) {
            this.mRecyclerAdapterWithHF = mRecyclerAdapterWithHF;
        }
    }


    private void initEmptyView() {
        if (null == commonEmptyView) {
            commonEmptyView = LayoutInflater.from(BaseLibConfig.getContext()).inflate(R.layout.common_empty_overlay, null);
            View rl_empty_overlay = commonEmptyView.findViewById(R.id.rl_empty_overlay);
            LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) rl_empty_overlay.getLayoutParams();
            if (AbPreconditions.checkNotNullRetureBoolean(mLayoutParams)) {
                mLayoutParams.weight = BaseLibConfig.UI_WIDTH;
                mLayoutParams.height = AbDisplayUtil.dip2px(minHeight);
            } else {
                mLayoutParams = new LinearLayout.LayoutParams(BaseLibConfig.UI_WIDTH, AbDisplayUtil.dip2px(minHeight));
            }
            rl_empty_overlay.setLayoutParams(mLayoutParams);
        }
    }


}
