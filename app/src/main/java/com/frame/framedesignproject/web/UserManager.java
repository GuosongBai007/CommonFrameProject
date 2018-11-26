package com.frame.framedesignproject.web;

import com.base.http.ApiServiceFactory;
import com.base.http.Http;
import com.base.http.interfaces.RequestListener;
import com.base.model.BaseResponse;
import com.frame.framedesignproject.interfaces.WebApi;
import com.frame.framedesignproject.model.UserInfo;

import io.reactivex.Observable;

/**
 * Author:GuosongBai
 * Date:2018/11/7 16:14
 * Description:
 */
public class UserManager {

    public static void getUserInfo(String userName, RequestListener<UserInfo> listener) {
        // 使用RxJava请求
        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("userPhone", userPhone);
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
            WebApi webApi = ApiServiceFactory.createService(WebApi.class);
            Observable<BaseResponse<UserInfo>> userInfoObservable = webApi.getUserInfo(userName);
            new Http<UserInfo>().requestApi(userInfoObservable, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
