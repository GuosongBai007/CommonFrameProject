package com.base.http;

import com.base.http.converter.GsonConverterFactory;
import com.base.utils.AppLogger;
import com.frame.baselibrary.BuildConfig;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Author:GuosongBai
 * Date:2018/11/7 14:23
 * Description:Retrofit 再次封装，返回结果使用Gson解析
 * 结合RxJava 使用
 */
public class ApiServiceFactory {
    private static Retrofit sRetrofit;

    public static <T> T createService(String baseUrl, Class<T> serviceClazz) {
        if (sRetrofit == null) {

            HttpLoggingInterceptor logger = new HttpLoggingInterceptor(message -> {
                AppLogger.d("http", message);
            });
            logger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (!BuildConfig.DEBUG) {
                // 非debug模式  禁止代理模式
                builder = builder.proxy(Proxy.NO_PROXY);
            }

            OkHttpClient client = builder
                    .addInterceptor(new AccessTokenInterceptor())
                    .addInterceptor(logger)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .build();

            sRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .client(client)
                    .build();
        }
        return sRetrofit.create(serviceClazz);
    }

    public static <T> T createService(Class<T> serviceClazz) {
        return createService(IpConfig.getBaseServerUrl(), serviceClazz);
    }

}
