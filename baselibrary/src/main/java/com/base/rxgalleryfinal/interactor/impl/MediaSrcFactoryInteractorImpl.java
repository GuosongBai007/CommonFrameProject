package com.base.rxgalleryfinal.interactor.impl;

import android.content.Context;

import com.base.rxgalleryfinal.bean.MediaBean;
import com.base.rxgalleryfinal.interactor.MediaSrcFactoryInteractor;
import com.base.rxgalleryfinal.utils.MediaUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 上午11:08
 */
public class MediaSrcFactoryInteractorImpl implements MediaSrcFactoryInteractor {

    private final Context context;
    private final OnGenerateMediaListener onGenerateMediaListener;
    private final boolean isImage;

    public MediaSrcFactoryInteractorImpl(Context context, boolean isImage, OnGenerateMediaListener onGenerateMediaListener) {
        this.context = context;
        this.isImage = isImage;
        this.onGenerateMediaListener = onGenerateMediaListener;
    }

    @Override
    public void generateMeidas(final String bucketId, final int page, final int limit) {
        Observable.create((ObservableOnSubscribe<List<MediaBean>>) subscriber -> {
            List<MediaBean> mediaBeanList = null;
            if (isImage) {
                mediaBeanList = MediaUtils.getMediaWithImageList(context, bucketId, page, limit);
            } else {
                mediaBeanList = MediaUtils.getMediaWithVideoList(context, bucketId, page, limit);
            }
            subscriber.onNext(mediaBeanList);
            subscriber.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<MediaBean>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGenerateMediaListener.onFinished(bucketId, page, limit, null);
                    }

                    @Override
                    public void onNext(List<MediaBean> mediaBeenList) {
                        onGenerateMediaListener.onFinished(bucketId, page, limit, mediaBeenList);
                    }
                });
    }
}
