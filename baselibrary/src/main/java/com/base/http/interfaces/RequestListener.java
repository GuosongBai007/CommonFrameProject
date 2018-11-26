package com.base.http.interfaces;

import com.base.model.BaseResponse;

/**
 * Author:GuosongBai
 * Date:2018/11/7 16:14
 * Description:
 */
public interface RequestListener<T> {

    void onComplete(BaseResponse<T> data);

}
