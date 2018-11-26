package com.base.model;

import java.util.List;

/**
 * Author:GuosongBai
 * Date:2018/11/7 16:07
 * Description: 网络请求基础类 所有请求model使用BaseResponse<T> 泛型就是真正获取的内容
 *  这个对象中的字段名必须和服务端定义好协议，字段名也必须和接口返回的字段名一致，否则会导致gson解析失败
 *  注意： 服务器每个接口都要统一格式
 */
public class BaseResponse<T> {

    private int code;
    private String message;
    private List<T> result;

    private boolean isSuccess;

    public BaseResponse() {

    }

    public BaseResponse(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
