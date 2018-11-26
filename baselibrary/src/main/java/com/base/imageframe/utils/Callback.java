package com.base.imageframe.utils;

/**
 * Author:GuosongBai
 * Date:2018/10/31 09:52
 * Description:
 */
public interface Callback<T> {
    int RESULT_SUCCESS = 0;
    int RESULT_FAILED = -1;
    int RESULT_SUCCESS_FROM_LOCAL = -2;

    void onTimeout(int taskId);

    void onCallback(int taskId, int result, T data);
}
