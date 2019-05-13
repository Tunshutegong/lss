package com.jiehun.component.widgets.pullrefresh;


import com.jiehun.component.utils.AbPreconditions;

import java.util.List;

/**
 * Created by zhouyao on 16-9-30.
 * 翻页的帮助类
 */
public class PagerHelper {

    private int pageSize = 14;//一页的大小
    private int initPageNum = 1;
    private int currentPageNum = 1;

    public PagerHelper() {

    }

    /**
     * @param pageSize    　一页的大小
     * @param initPageNum 页码初始化值　0或1等等
     */
    public PagerHelper(int pageSize, int initPageNum) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
        if (initPageNum > -1) {
            this.initPageNum = initPageNum;
            this.currentPageNum = initPageNum;
        }

    }

    public void resetPageNum() {
        currentPageNum = this.initPageNum;
    }

    /**
     * 自动加一页
     * @param addedData 添加的list数据；一般网咯返回的数据
     */
    public void addPageNum(List addedData) {
        if (!AbPreconditions.checkNotEmptyList(addedData)) {
            if (pageSize > addedData.size()) {
                return;
            }
            currentPageNum = currentPageNum + 1;
        }
    }


    public void addPageNum(int dataSize) {
        if (pageSize > dataSize) {
            return;
        }
        currentPageNum = currentPageNum + 1;
    }

    public void addPageNum(){
        currentPageNum = currentPageNum + 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public int getInitPageNum()
    {
        return initPageNum;
    }

}
