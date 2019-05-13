package com.jiehun.component.widgets.pullrefresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;


import com.jiehun.component.basiclib.R;
import com.jiehun.component.utils.AbPreconditions;
import com.jiehun.component.widgets.pullrefresh.loadmore.OnLoadMoreListener;
import com.jiehun.component.widgets.pullrefresh.loadmore.SwipeRefreshHelper;

import java.util.List;

/**
 * Created by zhouyao on 16-9-30.
 * <p/>
 * 　刷新相关
 * KkPullLayout.setPtrHandler(this);
 * KkPullLayout.refreshComplete();
 * 　KkPullLayout.isRefreshing
 * setPullToRefresh
 * <p/>
 * 自动调用刷新
 * KkPullLayout.autoRefresh();
 * <p/>
 * 加载更多
 * KkPullLayout.setLoadMoreEnable(true);
 * KkPullLayout.setOnLoadMoreListener(this)
 * 加载更多完成了
 * KkPullLayout.loadMoreComplete(boolean hasMore);
 * 设置没有数据可以通过
 * KkPullLayout.setNoMoreData(); 或　KkPullLayout.loadMoreComplete(false)
 * <p/>
 * 自动加载更多
 * KkPullLayout.setAutoLoadMoreEnable(isAutoLoadMoreEnable)
 */
public class PullRefreshHelper implements OnLoadMoreListener {

    public static final int PAGE_SIZE = 14;
    public static final int PAGE_NUM = 1;

    private   PagerHelper        mPagerHelper;//页码的帮助类
    protected SwipeRefreshHelper mSwipeRefreshHelper;//安卓5.0的SwipeRefresh下拉刷新帮助类
    private   IPullRefreshLister mIPullRefreshLister;//上下拉刷新的监听类

    private boolean isRreshEable = true;


    public boolean isRreshEable() {
        return isRreshEable;
    }

    /**
     * 禁止刷新
     * @param rreshEable
     */
    public void setRreshEnable(boolean rreshEable) {
        isRreshEable = rreshEable;
    }

    public PullRefreshHelper(IPullRefreshLister mIPullRefreshLister) {
        this.mPagerHelper = new PagerHelper();
        this.mIPullRefreshLister = mIPullRefreshLister;
    }

    public PullRefreshHelper(int pageSize, int initPageNum, IPullRefreshLister mIPullRefreshLister) {
        this.mPagerHelper = new PagerHelper(pageSize, initPageNum);
        this.mIPullRefreshLister = mIPullRefreshLister;
    }

    /**
     * 初始化帮助类
     * <p/>
     * 刷新相关
     * <p/>
     * mSwipeRefreshHelper.setOnSwipeRefreshListener(this);
     * mSwipeRefreshHelper.refreshComplete();
     * <p/>
     * 自动调用刷新
     * mSwipeRefreshHelper.autoRefresh();
     * <p/>
     * 加载更多
     * mSwipeRefreshHelper.setLoadMoreEnable(true);
     * mSwipeRefreshHelper.setOnLoadMoreListener(this)
     * 加载更多完成了
     * mSwipeRefreshHelper.loadMoreComplete(true);
     * 设置没有数据可以通过
     * mSwipeRefreshHelper.setNoMoreData(); 或　mSwipeRefreshHelper.loadMoreComplete(false)
     * <p/>
     * 自动加载更多
     * mSwipeRefreshHelper.setAutoLoadMoreEnable(isAutoLoadMoreEnable)
     *
     * @param mSryt
     */
    public void initRefreshView(SwipeRefreshLayout mSryt) {
        mSryt.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
        mSwipeRefreshHelper = new SwipeRefreshHelper(mSryt);
        mSwipeRefreshHelper.setOnSwipeRefreshListener(mOnSwipeRefreshListener);
        mSwipeRefreshHelper.setOnLoadMoreListener(this);
    }

    public void stopSwipeRefresh(){
        mSwipeRefreshHelper.refreshComplete();
    }

    /**
     * @param requestPage         　当前请求的页码
     * @param newlist
     * @param mSwipeRefreshHelper
     */
    public void listViewNotifyDataSetChanged(int requestPage, List newlist, SwipeRefreshHelper mSwipeRefreshHelper) {
        listViewNotifyDataSetChanged(getInitPageNum() == requestPage, newlist, mSwipeRefreshHelper);
    }

    public void listViewNotifyDataSetChanged(Throwable e, SwipeRefreshHelper mSwipeRefreshHelper) {
        if (null == e) {//onCompleted
            mSwipeRefreshHelper.refreshComplete();
            if (mSwipeRefreshHelper.hasInitLoadMoreView() && mSwipeRefreshHelper.isLoadMoreEnable()) {
                mSwipeRefreshHelper.loadMoreComplete(true);
            }
        } else {//onError
            mSwipeRefreshHelper.refreshComplete();
            if (mSwipeRefreshHelper.hasInitLoadMoreView() && mSwipeRefreshHelper.isLoadMoreEnable()) {
                mSwipeRefreshHelper.loadMoreComplete(true);
            }
        }

    }

    public void listViewNotifyDataSetChanged(Throwable e, PtrFrameLayout ptrClassicFrameLayout) {
        if (null == e) {//onCompleted
            ptrClassicFrameLayout.refreshComplete();
            if (ptrClassicFrameLayout.hasInitLoadMoreView() && ptrClassicFrameLayout.isLoadMoreEnable()) {
                ptrClassicFrameLayout.loadMoreComplete(true);
            }
        } else {//onError
            ptrClassicFrameLayout.refreshComplete();
            if (ptrClassicFrameLayout.hasInitLoadMoreView() && ptrClassicFrameLayout.isLoadMoreEnable()) {
                ptrClassicFrameLayout.loadMoreComplete(true);
            }
        }

    }

    /**
     * 仅支持　有上下拉功能的情况，只有刷新功能不能用该函数
     *
     * @param isRefresh
     * @param newlist
     * @param mSwipeRefreshHelper
     */
    public void listViewNotifyDataSetChanged(boolean isRefresh, List newlist, SwipeRefreshHelper mSwipeRefreshHelper) {
        if (null == mSwipeRefreshHelper) {
            return;
        }
        if (isRefresh) {//刷新操作
            mSwipeRefreshHelper.refreshComplete();
            if (AbPreconditions.checkNotEmptyList(newlist)) {
                resetPageNum();
                if (getPageSize() > newlist.size()) {//未满一页
                    mSwipeRefreshHelper.loadMoreComplete(false);
                } else {
                    mSwipeRefreshHelper.setLoadMoreEnable(true);
                }
            } else {//为空
                mSwipeRefreshHelper.setLoadMoreEnable(false);
            }
        } else {//加载更多

            if (AbPreconditions.checkNotEmptyList(newlist)) {
                if (getPageSize() > newlist.size()) {//未满一页
                    mSwipeRefreshHelper.loadMoreComplete(false);
                    mSwipeRefreshHelper.setLoadMoreEnable(false);
                    addPageNum();
                } else {//满了一页
                    mSwipeRefreshHelper.loadMoreComplete(true);
                    mSwipeRefreshHelper.setLoadMoreEnable(true);
                    addPageNum();
                }
            } else {
                mSwipeRefreshHelper.loadMoreComplete(false);
                mSwipeRefreshHelper.setLoadMoreEnable(false);
            }
        }
    }


    /**
     * SwipeRefresh　的帮助类
     *
     * @return
     */
    public SwipeRefreshHelper getSwipeRefreshHelper() {
        return mSwipeRefreshHelper;
    }

    @Override
    public void onLoadMore() {
        if (AbPreconditions.checkNotNullRetureBoolean(mIPullRefreshLister)) {
            mIPullRefreshLister.onLoadMore();
        }
    }

    private SwipeRefreshHelper.OnSwipeRefreshListener mOnSwipeRefreshListener = new SwipeRefreshHelper.OnSwipeRefreshListener() {
        @Override
        public void onRefreshBegin() {
//            onRefresh();
            if (AbPreconditions.checkNotNullRetureBoolean(mIPullRefreshLister)) {
                mIPullRefreshLister.onRefresh();
            }
        }
    };


    public void initRefreshView(PtrFrameLayout ptrClassicFrameLayout) {
        ptrClassicFrameLayout.disableWhenHorizontalMove(true);
        ptrClassicFrameLayout.setPtrHandler(mPtrDefaultHandler);
        ptrClassicFrameLayout.setOnLoadMoreListener(this);
    }


    /**
     * @param requestPage     　当前请求的页码
     * @param newlist
     * @param mPtrFrameLayout
     */
    public void listViewNotifyDataSetChanged(int requestPage, List newlist, PtrFrameLayout mPtrFrameLayout) {
        listViewNotifyDataSetChanged(getInitPageNum() == requestPage, newlist, mPtrFrameLayout);
    }


    /**
     * 仅支持　有上下拉功能的情况，只有刷新功能不能用该函数
     *
     * @param isRefresh
     * @param newlist
     * @param mPtrFrameLayout
     */
    public void listViewNotifyDataSetChanged(boolean isRefresh, List newlist, PtrFrameLayout mPtrFrameLayout) {
        if (null == mPtrFrameLayout) {
            return;
        }
        if (isRefresh) {//刷新操作
            mPtrFrameLayout.refreshComplete();
            if (AbPreconditions.checkNotEmptyList(newlist)) {
                resetPageNum();
                if (getPageSize() > newlist.size()) {//未满一页
                    if(!mPtrFrameLayout.hasInitLoadMoreView()){
                        mPtrFrameLayout.setLoadMoreEnable(true);
                        mPtrFrameLayout.setLoadMoreEnable(false);
                    }
                    mPtrFrameLayout.loadMoreComplete(false);
                } else {
                    mPtrFrameLayout.setLoadMoreEnable(true);
                }
            } else {//为空
                mPtrFrameLayout.setLoadMoreEnable(false);
            }
        } else {//加载更多

            if (AbPreconditions.checkNotEmptyList(newlist)) {
                if (getPageSize() > newlist.size()) {//未满一页
                    mPtrFrameLayout.setLoadMoreEnable(false);
                    mPtrFrameLayout.loadMoreComplete(false);
                    addPageNum();
                } else {//满了一页
                    mPtrFrameLayout.setLoadMoreEnable(true);
                    mPtrFrameLayout.loadMoreComplete(true);
                    addPageNum();
                }
            } else {
                mPtrFrameLayout.setLoadMoreEnable(false);
                mPtrFrameLayout.loadMoreComplete(false);
            }
        }
    }

    /**
     * 每页不固定条目的列表刷新
     * @param isRefresh
     * @param newList
     * @param mPtrFrameLayout
     */
    public void notifyDataSetChanged(boolean isRefresh, List newList, PtrFrameLayout mPtrFrameLayout) {
        if (null == mPtrFrameLayout) {
            return;
        }
        mPtrFrameLayout.refreshComplete();
        if (newList == null || newList.size() == 0) {
            mPtrFrameLayout.setLoadMoreEnable(true);
            mPtrFrameLayout.setLoadMoreEnable(false);
            mPtrFrameLayout.loadMoreComplete(false);
        } else {
            mPtrFrameLayout.setLoadMoreEnable(true);
            mPtrFrameLayout.loadMoreComplete(true);
        }
    }

    /**
     * PtrFrameLayout的刷新
     */
    private PtrDefaultHandler mPtrDefaultHandler = new PtrDefaultHandler() {
        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            if (AbPreconditions.checkNotNullRetureBoolean(mIPullRefreshLister)) {
                mIPullRefreshLister.onRefresh();
            }
        }

        @Override
        public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

            if(isRreshEable) {

                return super.checkCanDoRefresh(frame, content, header);
            }
            return false;
        }
    };


    public void resetPageNum() {
        if (null != mPagerHelper) {
            mPagerHelper.resetPageNum();
        }
    }

    public void addPageNum(List addedData) {
        if (null != mPagerHelper) {
            mPagerHelper.addPageNum(addedData);
        }
    }

    public void addPageNum(int dataSize) {
        if (null != mPagerHelper) {
            mPagerHelper.addPageNum(dataSize);
        }
    }

    public void addPageNum() {
        if (null != mPagerHelper) {
            mPagerHelper.addPageNum();
        }
    }

    public int getPageSize() {
        if (null != mPagerHelper) {
            return mPagerHelper.getPageSize();
        }
        return 0;
    }

    public int getInitPageNum() {
        if (null != mPagerHelper) {
            return mPagerHelper.getInitPageNum();
        }
        return 0;
    }

    public int getCurrentPageNum() {
        if (null != mPagerHelper) {
            return mPagerHelper.getCurrentPageNum();
        }
        return 0;
    }

    public int getLoadMorePageNum(){
        return getCurrentPageNum() + 1;
    }
}
