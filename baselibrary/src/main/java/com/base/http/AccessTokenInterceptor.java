package com.base.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author:GuosongBai
 * Date:2018/11/7 14:22
 * Description: Okhttp 拦截器
 * 用于token操作
 */
public class AccessTokenInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        // 这里可以做一些拦截工作  比如加入  解密   重写请求头  刷新token等

        return chain.proceed(chain.request());
    }
}
