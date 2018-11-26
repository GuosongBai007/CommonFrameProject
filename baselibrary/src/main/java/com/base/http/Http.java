package com.base.http;

import com.base.http.interfaces.RequestListener;
import com.base.model.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:GuosongBai
 * Date:2018/11/7 16:30
 * Description:
 */
public class Http<T> {

    public void requestApi(Observable<BaseResponse<T>> observable, RequestListener<T> listener) {
        /**
         * 这个地方Rxjava 的请求和回调都放在io线程中  至于回调具体在哪个线程 开发人员在接口回调中自己转换
         */
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<BaseResponse<T>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<T> t) {
                        t.setSuccess(true);
                        listener.onComplete(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onComplete(new BaseResponse(false));
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
