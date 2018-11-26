package com.base.rxgalleryfinal.interactor;

import com.base.rxgalleryfinal.bean.MediaBean;

import java.util.List;


/**
 * Desction:媒体资源工厂
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 上午11:06
 */
public interface MediaSrcFactoryInteractor {

    /**
     * 生产资源
     */
    void generateMeidas(String bucketId, int page, int limit);

    interface OnGenerateMediaListener {
        void onFinished(String bucketId, int pageSize, int currentOffset, List<MediaBean> list);
    }

}
