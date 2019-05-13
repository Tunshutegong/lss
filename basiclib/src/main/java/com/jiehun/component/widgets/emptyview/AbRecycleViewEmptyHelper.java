package com.jiehun.component.widgets.emptyview;

import android.support.annotation.DrawableRes;
import android.view.View;


import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.widgets.recycleview.RecyclerAdapterWithHF;

import java.util.List;

/**
 * Created by zhouyao on 17-12-18.
 * 设置Recyclerview带头部的空白页等等
 */

public class AbRecycleViewEmptyHelper {

    /******************** 设置Recyclerview带头部的空白页等等 *************************/

    private RecyclerEmptyView     mRecyclerEmptyView;//Recyclerview带头部的空白页
    /**
     * 设置空白页等等
     */

    private RecyclerAdapterWithHF mRecyclerAdapterWithHF;

    public AbRecycleViewEmptyHelper(RecyclerAdapterWithHF mRecyclerAdapterWithHF) {
        this.mRecyclerAdapterWithHF = mRecyclerAdapterWithHF;
    }

    private synchronized RecyclerEmptyView getRecyclerEmptyView() {
        if (!AbPreconditions.checkNotNullRetureBoolean(mRecyclerEmptyView)) {
            mRecyclerEmptyView = new RecyclerEmptyView();
        }
        return mRecyclerEmptyView;
    }

    public void setRecyclerWithHeadEmptyView(View.OnClickListener mOnClickListener) {
        getRecyclerEmptyView().setEmptyView(mOnClickListener, -1, -1);
    }

    public void setRecyclerWithHeadEmptyView(View.OnClickListener mOnClickListener, int headHeightOfDp, int marginTopOfDp) {
        getRecyclerEmptyView().setEmptyView(mOnClickListener, headHeightOfDp, marginTopOfDp);
    }

    public void setRecyclerWithHeadViewData(CharSequence emptyData, @DrawableRes int imgRes) {
        getRecyclerEmptyView().setEmptyViewData(emptyData, imgRes);
    }

    public void showOverView(int listSize){
        if (listSize > 0) {
            hideRecyclerWithHeadEmptyView();
        } else {
            showRecyclerWithHeadEmptyView();
        }
    }

    public void showOverView(List list) {
        if (AbPreconditions.checkNotEmptyList(list)) {
            hideRecyclerWithHeadEmptyView();
        } else {
            showRecyclerWithHeadEmptyView();
        }
    }


    public void showRecyclerWithHeadEmptyView() {
        getRecyclerEmptyView().showEmptyView(mRecyclerAdapterWithHF);
    }

    public void hideRecyclerWithHeadEmptyView(){
        getRecyclerEmptyView().hideEmptyView(mRecyclerAdapterWithHF);
    }

}
