package com.jiehun.component.eventbus;

import lombok.Data;

/**
 * Created by zhouyao
 * on 2017/12/13.
 */

@Data
public class BaseResponse<T> {

    private int what;
    private int cmd;
    //返回状态 0 成功
    private int code;
    private String message = "";
    // 返回信息
    private T data;
}
