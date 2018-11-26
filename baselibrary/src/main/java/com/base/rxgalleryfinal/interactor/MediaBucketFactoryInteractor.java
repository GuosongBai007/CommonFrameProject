package com.base.rxgalleryfinal.interactor;

import com.base.rxgalleryfinal.bean.BucketBean;

import java.util.List;


/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/4 下午8:24
 */
public interface MediaBucketFactoryInteractor {

    void generateBuckets();

    interface OnGenerateBucketListener {
        void onFinished(List<BucketBean> list);
    }
}
