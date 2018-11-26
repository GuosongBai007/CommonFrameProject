package com.frame.framedesignproject.interfaces;

import com.base.model.BaseResponse;
import com.frame.framedesignproject.model.UserInfo;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author:GuosongBai
 * Date:2018/11/7 15:56
 * Description: 实际请求中的api
 */
public interface WebApi {
//    http://api.apiopen.top/searchAuthors?name=李白

    @GET("searchAuthors")
    Observable<BaseResponse<UserInfo>> getUserInfo(@Query("name") String name);

}
