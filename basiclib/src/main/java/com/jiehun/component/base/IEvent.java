package com.jiehun.component.base;

import com.jiehun.component.eventbus.BaseResponse;

/**
 * dmp_hunbohui
 * describe:
 * author liulj
 * date 2018/1/31
 */

public interface IEvent {
    void onReceive(BaseResponse baseResponse);
    void post(int event);
}
